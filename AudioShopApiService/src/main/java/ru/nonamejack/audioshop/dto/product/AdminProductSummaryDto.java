package ru.nonamejack.audioshop.dto.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

public record AdminProductSummaryDto(
        Integer productId,
        String brandName,
        String name,
        BigDecimal price,
        Integer stock,
        String mainImage,
        Boolean active
) {}
