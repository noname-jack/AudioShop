package ru.nonamejack.audioshop.dto.attribute;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class StringAttributeDto extends AttributeProductDto{
    private String value;
}
