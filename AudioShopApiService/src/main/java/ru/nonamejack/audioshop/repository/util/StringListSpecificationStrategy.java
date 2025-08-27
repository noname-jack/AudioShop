package ru.nonamejack.audioshop.repository.util;

import ru.nonamejack.audioshop.dto.request.AttributeValueFilter;
import ru.nonamejack.audioshop.model.enums.FilterType;
import ru.nonamejack.audioshop.model.Product;
import ru.nonamejack.audioshop.model.ProductAttribute;
import ru.nonamejack.audioshop.model.enums.ValueType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class StringListSpecificationStrategy implements AttributeFilterSpecificationStrategy{
    @Override
    public boolean supports(AttributeValueFilter filter) {
        return filter.filterType() == FilterType.ENUM
                && filter.valueType() == ValueType.STRING
                && filter.stringValues() != null
                && !filter.stringValues().isEmpty();
    }

    @Override
    public Specification<Product> toSpecification(AttributeValueFilter filter) {
        return (root, query, cb) -> {
            Join<Product, ProductAttribute> join =
                    root.join("productAttributeList", JoinType.INNER);
            Predicate byId = cb.equal(
                    join.get("attribute").get("attributeId"),
                    filter.attributeId()
            );
            return cb.and(byId, join.get("stringValue").in(filter.stringValues()));
        };
    }
}
