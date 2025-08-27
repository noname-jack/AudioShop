package ru.nonamejack.audioshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="product_attributes")
@Getter
@Setter
public class ProductAttribute {
    @EmbeddedId
    private ProductAttributeKey id;

    @ManyToOne
    @MapsId("attributeId")
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "double_value")
    private Double doubleValue;

    @Column(name = "string_value")
    private String stringValue;

    @Column(name = "boolean_value")
    private Boolean booleanValue;
}
