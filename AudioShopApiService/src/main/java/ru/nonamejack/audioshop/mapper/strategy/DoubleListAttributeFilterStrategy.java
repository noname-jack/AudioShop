package ru.nonamejack.audioshop.mapper.strategy;

import ru.nonamejack.audioshop.dto.attribute.AttributeCategoryDto;
import ru.nonamejack.audioshop.dto.attribute.DoubleListAttributeFilterDto;
import ru.nonamejack.audioshop.mapper.AttributeMapper;
import ru.nonamejack.audioshop.model.enums.FilterType;
import ru.nonamejack.audioshop.model.enums.ValueType;
import ru.nonamejack.audioshop.repository.ProductAttributeRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DoubleListAttributeFilterStrategy implements AttributeFilterStrategy {
    private final ProductAttributeRepository paRepo;
    private final AttributeMapper mapper;

    public DoubleListAttributeFilterStrategy(ProductAttributeRepository paRepo, AttributeMapper mapper) {
        this.paRepo = paRepo;
        this.mapper = mapper;
    }

    @Override
    public boolean supports(AttributeCategoryDto base) {
        return base.getFilterType() != null
                && base.getFilterType().equals(FilterType.ENUM)
                && base.getValueType() != null
                && base.getValueType().equals(ValueType.DOUBLE);
    }

    @Override
    public AttributeCategoryDto build(AttributeCategoryDto base, Integer categoryId) {
        List<Double> values = paRepo.findDistinctDoubleValues(categoryId, base.getAttributeId());
        DoubleListAttributeFilterDto dto = mapper.toDoubleListFilter(base);
        dto.setAvailableValuesDouble(values);
        return dto;
    }
}
