package ru.nonamejack.audioshop.dto.attribute;


import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
@Setter
public class DoubleAttributeDto extends AttributeProductDto{
    private Double value;
}
