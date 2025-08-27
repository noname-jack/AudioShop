package ru.nonamejack.audioshop.dto.request;

import jakarta.validation.constraints.NotNull;

public record CategoryAttributeDtoRequest(
        @NotNull
        Boolean isRequired,
        @NotNull
        Boolean isUseFilter
) {
}
