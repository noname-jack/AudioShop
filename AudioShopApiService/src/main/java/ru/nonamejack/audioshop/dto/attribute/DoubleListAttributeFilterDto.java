package ru.nonamejack.audioshop.dto.attribute;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class DoubleListAttributeFilterDto extends AttributeCategoryDto{
    private List<Double> availableValuesDouble;
}
