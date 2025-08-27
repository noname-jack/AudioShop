package ru.nonamejack.audioshop.mapper.strategy;

import ru.nonamejack.audioshop.dto.attribute.AttributeCategoryDto;
import ru.nonamejack.audioshop.dto.attribute.DoubleRangeAttributeDto;
import ru.nonamejack.audioshop.mapper.AttributeMapper;
import ru.nonamejack.audioshop.model.enums.FilterType;
import ru.nonamejack.audioshop.model.enums.ValueType;
import ru.nonamejack.audioshop.repository.ProductAttributeRepository;
import ru.nonamejack.audioshop.util.IRange;
import org.springframework.stereotype.Component;

@Component
public class DoubleRangeAttributeFilterStrategy implements AttributeFilterStrategy{
    private final ProductAttributeRepository paRepo;
    private final AttributeMapper mapper;

    public DoubleRangeAttributeFilterStrategy(ProductAttributeRepository paRepo, AttributeMapper mapper) {
        this.paRepo = paRepo;
        this.mapper = mapper;
    }

    @Override
    public boolean supports(AttributeCategoryDto base) {
        return base.getFilterType() != null
                && base.getFilterType().equals(FilterType.RANGE)
                && base.getValueType() != null
                && base.getValueType().equals(ValueType.DOUBLE);
    }

    @Override
    public AttributeCategoryDto build(AttributeCategoryDto base, Integer categoryId) {
        IRange r = paRepo.findRange(categoryId, base.getAttributeId());
        DoubleRangeAttributeDto dto = mapper.toRangeFilter(base);
        dto.setMinValue(r.getMinValue());
        dto.setMaxValue(r.getMaxValue());
        return dto;
    }
}
