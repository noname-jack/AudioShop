package ru.nonamejack.audioshop.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nonamejack.audioshop.dto.cart.CartDto;
import ru.nonamejack.audioshop.dto.request.AddCartItemRequest;
import ru.nonamejack.audioshop.exception.custom.NotFoundException;
import ru.nonamejack.audioshop.mapper.CartMapper;
import ru.nonamejack.audioshop.model.Cart;
import ru.nonamejack.audioshop.model.CartItem;
import ru.nonamejack.audioshop.model.Product;
import ru.nonamejack.audioshop.model.User;
import ru.nonamejack.audioshop.repository.CartRepository;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartMapper cartMapper;
    private final ProductService productService;

    private final MessageService messageService;
    public CartService(CartRepository cartRepository, UserService userService, CartMapper cartMapper, ProductService productService, MessageService messageService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.cartMapper = cartMapper;
        this.productService = productService;
        this.messageService = messageService;
    }

    @Transactional()
    public CartDto getCartByUserId(Integer userId) {
        Cart cart = getOrCreateCart(userId);
        return cartMapper.toCartDto(cart);
    }

    @Transactional
    public CartDto addProductToCart(Integer userId, AddCartItemRequest request) {
        Cart cart = getOrCreateCart(userId);
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(request.productId()))
                .findFirst();
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.quantity());
        }
        else{
            Product product = productService.getProductById(request.productId());
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.quantity());
            cart.getItems().add(newItem);
        }
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartDto(savedCart);
    }

    @Transactional
    public CartDto updateItemQuantity(Integer userId, Integer itemId, Integer newQuantity) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getCartItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(messageService.getMessage("error.cart.item.not_found")));
        if (newQuantity <= 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(newQuantity);
        }
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartDto(savedCart);
    }


    @Transactional
    public CartDto removeItem(Integer userId, Integer itemId) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getCartItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(messageService.getMessage("error.cart.item.not_found")));
        cart.getItems().remove(item);

        cartRepository.save(cart);
        return cartMapper.toCartDto(cart);
    }
    @Transactional
    public CartDto clearCart(Integer userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
        return cartMapper.toCartDto(cart);
    }

    private Cart getOrCreateCart(Integer userId) {
        User user = userService.getCurrentUserById(userId);
        return cartRepository.findByUser(user).orElseGet(() -> createEmptyCart(user));
    }
    private Cart createEmptyCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }
}
