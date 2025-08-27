package ru.nonamejack.audioshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.nonamejack.audioshop.model.enums.FilterType;
import ru.nonamejack.audioshop.model.enums.ValueType;

public record AttributeDtoRequest(
        @NotBlank
        String name,

        @NotNull
        ValueType valueType,
        @NotNull
        FilterType filterType
) {
}
