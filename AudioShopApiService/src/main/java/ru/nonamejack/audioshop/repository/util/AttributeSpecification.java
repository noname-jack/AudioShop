package ru.nonamejack.audioshop.repository.util;

import org.springframework.data.jpa.domain.Specification;
import ru.nonamejack.audioshop.model.Attribute;
import ru.nonamejack.audioshop.model.enums.ValueType;

public class AttributeSpecification {

    public static Specification<Attribute> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Attribute> hasValueType(ValueType valueType) {
        return (root, query, criteriaBuilder) -> {
            if (valueType == null) {
                return criteriaBuilder.conjunction();
            } else {
                return criteriaBuilder.equal(root.get("valueType"), valueType);
            }
        };
    }

    public static Specification<Attribute> buildSpecification(String name, ValueType valueType){
        return Specification.where(hasName(name))
                .and(hasValueType(valueType));
    }
}
