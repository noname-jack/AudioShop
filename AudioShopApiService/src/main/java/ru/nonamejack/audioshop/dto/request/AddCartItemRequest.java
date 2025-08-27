package ru.nonamejack.audioshop.dto.request;

import jakarta.validation.constraints.Positive;

public record AddCartItemRequest(
        @Positive
        Integer productId,

        @Positive
        Integer quantity
) {
}
