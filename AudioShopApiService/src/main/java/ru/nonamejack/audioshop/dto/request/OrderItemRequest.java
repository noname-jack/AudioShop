package ru.nonamejack.audioshop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;


public record OrderItemRequest(
        @Positive Integer productId,
        @Min(1) int quantity
) {}