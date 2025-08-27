package ru.nonamejack.audioshop.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Перечисление типов фильтрации атрибута")
@Getter
public enum FilterType {
    @Schema(description = "Фильтрация не задана")
    UNKNOWN(0),
    @Schema(description = "Фильтрация по диапазону чисел (RANGE)")
    RANGE(1),

    @Schema(description = "Фильтрация по списку значений (ENUM)")
    ENUM(2),

    @Schema(description = "Фильтрация по булевому признаку (BOOLEAN)")
    BOOLEAN(3);

    private final int code;

    FilterType(int code){
        this.code = code;
    }
    public static FilterType fromCode(int code) {
        for (FilterType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid FilterType code: " + code);
    }
}