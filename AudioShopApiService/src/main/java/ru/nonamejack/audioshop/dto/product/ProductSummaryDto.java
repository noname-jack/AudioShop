package ru.nonamejack.audioshop.dto.product;

import java.math.BigDecimal;


public record ProductSummaryDto(
        Integer productId,
        String brandName,
        String name,
        BigDecimal price,
        Integer stock,
        String mainImage
) {}