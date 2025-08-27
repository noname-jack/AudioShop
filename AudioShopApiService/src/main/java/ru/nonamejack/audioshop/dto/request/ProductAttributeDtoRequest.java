package ru.nonamejack.audioshop.dto.request;

import jakarta.validation.constraints.NotNull;

public record ProductAttributeDtoRequest(
        Double doubleValue,
        String stringValue,
        Boolean booleanValue
) {}
