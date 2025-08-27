package ru.nonamejack.audioshop.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeKey implements Serializable {
    private Integer productId;
    private Integer attributeId;
}
