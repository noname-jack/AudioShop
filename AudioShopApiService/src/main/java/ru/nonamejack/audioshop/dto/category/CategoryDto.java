package ru.nonamejack.audioshop.dto.category;



public record CategoryDto(
        Integer categoryId,
        Integer parentCategoryId,
        String name,
        String description,
        String imgPath
) {}
