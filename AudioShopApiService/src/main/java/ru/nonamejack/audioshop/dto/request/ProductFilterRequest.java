package ru.nonamejack.audioshop.dto.request;

import ru.nonamejack.audioshop.dto.BigDecimalRangeDto;
import ru.nonamejack.audioshop.dto.DoubleRangeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ProductFilterRequest(
        @NotNull(message = "обязательна категория")
        Integer categoryId,
        List<Integer> brandIds,
        BigDecimalRangeDto priceRange,

        // Список выбранных значений атрибутов.
        // Для ENUM: список строк/чисел/булевых,
        // для RANGE: можно хранить тоже RangeDto<Double>
        @Valid
        List<AttributeValueFilter> attributeFilters
) {}

