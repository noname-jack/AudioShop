package ru.nonamejack.audioshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="category_attributes")
public class CategoryAttribute {
    @EmbeddedId
    private CategoryAttributeKey categoryAttributeKey;

    @ManyToOne()
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne()
    @MapsId("attributeId")
    @JoinColumn(name = "attribute_id", nullable = false)
    private Attribute attribute;

    @Column(name="is_required", nullable = false)
    private boolean required;

    @Column(name = "is_use_filter",nullable = false)
    private boolean useFilter;
}
