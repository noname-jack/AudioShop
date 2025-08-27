package ru.nonamejack.audioshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BrandDtoRequest(
        @NotNull
        @NotBlank
        String name,
        String shortDescription,
        String description
) {}
