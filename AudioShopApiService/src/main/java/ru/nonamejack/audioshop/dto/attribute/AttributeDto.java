package ru.nonamejack.audioshop.dto.attribute;

import ru.nonamejack.audioshop.model.enums.FilterType;
import ru.nonamejack.audioshop.model.enums.ValueType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Характеристика товара")
public class AttributeDto {

    @Schema(description = "ID характеристики", example = "10")
    private Integer attributeId;

    @Schema(description = "Название характеристики", example = "Частота, Гц")
    private String attributeName;

    @Schema(
            description    = "Тип значения характеристики",
            allowableValues = {"DOUBLE","STRING","BOOLEAN"}
    )
    private ValueType valueType;

    private FilterType filterType;
}