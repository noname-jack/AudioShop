package ru.nonamejack.audioshop.dto.cart;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

public record CartDto(
        Integer cartId,
        List<CartItemDto> cartItems,
        BigDecimal totalPrice,
        Integer totalItems
) {}
