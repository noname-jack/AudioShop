package ru.nonamejack.audioshop.dto.cart;
import java.math.BigDecimal;
public record CartItemDto(
        Integer cartItemId,
        Integer productId,
        Integer quantity,
        BigDecimal price,
        String productName,
        String productImage) {
}
