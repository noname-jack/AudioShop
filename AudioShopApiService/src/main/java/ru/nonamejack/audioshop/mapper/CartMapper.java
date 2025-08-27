package ru.nonamejack.audioshop.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.nonamejack.audioshop.dto.cart.CartDto;
import ru.nonamejack.audioshop.dto.cart.CartItemDto;
import ru.nonamejack.audioshop.model.Cart;
import ru.nonamejack.audioshop.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "cartId", source = "cartId")
    @Mapping(target = "cartItems", source = "items")
    @Mapping(target = "totalItems", expression = "java(calculateTotalItems(cart.getItems()))")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(cart.getItems()))")
    CartDto toCartDto(Cart cart);


    @Mapping(target = "cartItemId", source = "cartItem.cartItemId")
    @Mapping(target = "productId", source = "cartItem.product.productId")
    @Mapping(target = "quantity", source = "cartItem.quantity")
    @Mapping(target = "price", source = "cartItem.product.price")
    @Mapping(target = "productName", source = "cartItem.product.name")
    @Mapping(target = "productImage", source = "cartItem.product.mainImage")
    CartItemDto toCartItemDto(CartItem cartItem);
    List<CartItemDto> toCartItemDtoList(List<CartItem> items);

    default Integer calculateTotalItems(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return 0;
        } else {
            return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
        }
    }

    default BigDecimal calculateTotalPrice(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return cartItems.stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }




}

