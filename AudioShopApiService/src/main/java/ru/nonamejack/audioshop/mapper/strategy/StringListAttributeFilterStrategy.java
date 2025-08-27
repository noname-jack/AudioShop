package ru.nonamejack.audioshop.mapper.strategy;

import ru.nonamejack.audioshop.dto.attribute.AttributeCategoryDto;
import ru.nonamejack.audioshop.dto.attribute.StringListAttributeFilterDto;
import ru.nonamejack.audioshop.mapper.AttributeMapper;
import ru.nonamejack.audioshop.model.enums.FilterType;
import ru.nonamejack.audioshop.model.enums.ValueType;
import ru.nonamejack.audioshop.repository.ProductAttributeRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StringListAttributeFilterStrategy implements AttributeFilterStrategy {
    private final ProductAttributeRepository paRepo;
    private final AttributeMapper mapper;

    public StringListAttributeFilterStrategy(ProductAttributeRepository paRepo, AttributeMapper mapper) {
        this.paRepo = paRepo;
        this.mapper = mapper;
    }

    @Override
    public boolean supports(AttributeCategoryDto base) {
        return base.getFilterType() != null
                && base.getFilterType().equals(FilterType.ENUM)
                && base.getValueType() != null
                && base.getValueType().equals(ValueType.STRING);
    }

    @Override
    public StringListAttributeFilterDto build(AttributeCategoryDto base, Integer categoryId) {
        List<String> values = paRepo.findDistinctStringValues(categoryId, base.getAttributeId());
        StringListAttributeFilterDto dto = mapper.toStringFilter(base);
        dto.setAvailableValuesString(values);
        return dto;
    }
}
