package ru.nonamejack.audioshop.repository.util;

import ru.nonamejack.audioshop.dto.request.AttributeValueFilter;
import ru.nonamejack.audioshop.model.Product;
import org.springframework.data.jpa.domain.Specification;

public interface AttributeFilterSpecificationStrategy {
    boolean supports(AttributeValueFilter filter);

    Specification<Product> toSpecification(AttributeValueFilter filter);
}
