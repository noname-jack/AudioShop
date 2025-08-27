package ru.nonamejack.audioshop.mapper.strategy;

import ru.nonamejack.audioshop.dto.attribute.AttributeCategoryDto;

public interface AttributeFilterStrategy {
    boolean supports(AttributeCategoryDto base);
    AttributeCategoryDto build(AttributeCategoryDto base, Integer categoryId);
}
