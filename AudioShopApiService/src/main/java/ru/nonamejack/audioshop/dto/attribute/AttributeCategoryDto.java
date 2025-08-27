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
@Schema(description = "Атрибут категории для настройки фильтрации")
public class AttributeCategoryDto {

    @Schema(description = "Идентификатор атрибута", example = "45")
    private Integer attributeId;

    @Schema(description = "Название атрибута", example = "Типоразмер")
    private String attributeName;

    @Schema(
            description = "Тип значения атрибута",
            allowableValues = {"DOUBLE", "STRING", "BOOLEAN"}
    )
    private ValueType valueType;

    @Schema(
            description = "Тип фильтрации для атрибута",
            allowableValues = {"ENUM", "RANGE", "BOOLEAN"}
    )
    private FilterType filterType;

    @Schema(description = "Является ли атрибут обязательным для товаров категории", example = "true")
    private boolean required;

    @Schema(description = "Является ли атрибут обязательным для фильтрации", example = "false")
    private boolean useFilter;
}