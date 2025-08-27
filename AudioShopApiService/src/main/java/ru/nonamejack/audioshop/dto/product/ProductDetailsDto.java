package ru.nonamejack.audioshop.dto.product;

import ru.nonamejack.audioshop.dto.attribute.AttributeDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public record ProductDetailsDto(
        Integer productId,
        String brandName,
        Integer brandId,
        String categoryName,
        String categoryId,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String mainImage,
        Map<Integer, String> imagesMap,
        List<AttributeDto> attributes,
        List<ReviewDto> reviews,
        Boolean active
) {}
