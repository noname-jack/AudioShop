package ru.nonamejack.audioshop.dto.brand;



public record BrandDto(
        Integer id,
        String name,
        String imagePath,
        String shortDescription,
        String description
) {}
