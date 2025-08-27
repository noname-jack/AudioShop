package ru.nonamejack.audioshop.dto.request;

import ru.nonamejack.audioshop.dto.DoubleRangeDto;
import ru.nonamejack.audioshop.model.enums.FilterType;
import ru.nonamejack.audioshop.model.enums.ValueType;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AttributeValueFilter(
        @NotNull Integer attributeId,
        @NotNull FilterType filterType,
        @NotNull ValueType valueType,
        List<String> stringValues,
        List<Double> doubleValues,
        List<Boolean> booleanValues,
        DoubleRangeDto rangeValue
) {}