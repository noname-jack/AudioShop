package ru.nonamejack.audioshop.repository.util;

import ru.nonamejack.audioshop.dto.request.AttributeValueFilter;
import ru.nonamejack.audioshop.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AttributeFilterSpecificationFactory {
    private final List<AttributeFilterSpecificationStrategy> strategies;

    public AttributeFilterSpecificationFactory(
            List<AttributeFilterSpecificationStrategy> strategies
    ) {
        this.strategies = strategies;
    }

    public Specification<Product> createSpecification(AttributeValueFilter filter) {
        return strategies.stream()
                .filter(s -> s.supports(filter))
                .findFirst()
                .map(s -> s.toSpecification(filter))
                .orElse((root, query, cb) -> cb.conjunction());
    }
}
