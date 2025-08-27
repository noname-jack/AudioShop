package ru.nonamejack.audioshop.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest (
        @NotBlank
        String name,
        String description,
        Integer parentCategoryId
){
}
