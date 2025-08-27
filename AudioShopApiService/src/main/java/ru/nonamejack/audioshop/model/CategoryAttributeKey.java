package ru.nonamejack.audioshop.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAttributeKey implements Serializable {
    private Integer categoryId;
    private Integer attributeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryAttributeKey that = (CategoryAttributeKey) o;
        return Objects.equals(categoryId, that.categoryId) && Objects.equals(attributeId, that.attributeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, attributeId);
    }
}
