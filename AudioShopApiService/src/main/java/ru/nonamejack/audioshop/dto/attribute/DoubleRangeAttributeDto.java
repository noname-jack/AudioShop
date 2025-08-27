package ru.nonamejack.audioshop.dto.attribute;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class DoubleRangeAttributeDto extends AttributeCategoryDto{
    private double minValue;
    private double maxValue;
}
