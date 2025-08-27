package ru.nonamejack.audioshop.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Schema(description = "Перечисление типов значений атрибута")
@Getter
public enum ValueType {
    @Schema(description = "Неизвестное значение")
    UNKNOWN(0),

    @Schema(description = "Числовое значение")
    DOUBLE(1),

    @Schema(description = "Строковое значение")
    STRING(2),

    @Schema(description = "Логическое значение")
    BOOLEAN(3);

    private final int code;

    ValueType(int code) {
        this.code = code;
    }
    public static ValueType fromCode(int code) {
        for (ValueType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid ValueType code: " + code);
    }
}