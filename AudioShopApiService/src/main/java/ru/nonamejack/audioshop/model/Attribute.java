package ru.nonamejack.audioshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.nonamejack.audioshop.model.enums.FilterType;
import ru.nonamejack.audioshop.model.enums.ValueType;

@Entity
@Table(name = "attributes")
@Getter
@Setter
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attributeId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, length = 50)
    private ValueType valueType;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, length = 20)
    private FilterType filterType;
}
