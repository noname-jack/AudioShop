package ru.nonamejack.audioshop.dto.attribute;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class BooleanAttributeDto extends AttributeProductDto {
    private Boolean value;
}
