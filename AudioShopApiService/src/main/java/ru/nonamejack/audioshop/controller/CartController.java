package ru.nonamejack.audioshop.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.nonamejack.audioshop.dto.ApiResponse;
import ru.nonamejack.audioshop.dto.cart.CartDto;
import ru.nonamejack.audioshop.dto.request.AddCartItemRequest;
import ru.nonamejack.audioshop.service.CartService;
import ru.nonamejack.audioshop.util.SecurityContextUtil;

@RestController
@RequestMapping("/cart")
@Validated
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping()
    public ApiResponse<CartDto> getCart() {
        Integer userId = SecurityContextUtil.getCurrentUser();
        return ApiResponse.success(cartService.getCartByUserId(userId));
    }

    @PostMapping("/items")
    public ApiResponse<CartDto> addItemToCart(@RequestBody @Valid AddCartItemRequest request) {
        Integer userId = SecurityContextUtil.getCurrentUser();
        return ApiResponse.success(cartService.addProductToCart(userId, request));
    }

    @PutMapping("/items/{itemId}")
    public ApiResponse<CartDto> updateItemQuantity(@PathVariable @Positive Integer itemId, @RequestParam() Integer quantity) {
        Integer userId = SecurityContextUtil.getCurrentUser();
        return ApiResponse.success(cartService.updateItemQuantity(userId, itemId, quantity));
    }


    @DeleteMapping("/items")
    public ApiResponse<CartDto> clearCart() {
        Integer userId = SecurityContextUtil.getCurrentUser();
        return ApiResponse.success(cartService.clearCart(userId));
    }

    @DeleteMapping("/items/{itemId}")
    public ApiResponse<CartDto> removeItem(@PathVariable @Positive Integer itemId) {
        Integer userId = SecurityContextUtil.getCurrentUser();
        return ApiResponse.success(cartService.removeItem(userId, itemId));
    }


}
