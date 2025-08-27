package ru.nonamejack.audioshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductDtoRequest(
        @NotNull
        Integer categoryId,

        @NotNull
        Integer brandId,

        @NotBlank
        String name,


        String description,

        @NotNull
        BigDecimal price,

        @NotNull
        Integer stock,
        @NotNull
        Boolean active) {

}
