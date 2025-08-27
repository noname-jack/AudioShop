package ru.nonamejack.audioshop.repository.util;

import org.springframework.data.jpa.domain.Specification;
import ru.nonamejack.audioshop.model.Category;

public class CategorySpecification {

    public static Specification<Category> hasName(String name) {
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

    public static Specification<Category> hasParentId(Integer parentId) {
        return (root, query, criteriaBuilder) -> {
            if (parentId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("parentCategoryId"), parentId);
        };
    }

    public static Specification<Category> buildSpecification(String name, Integer parentId) {
        return Specification.where(hasName(name))
                .and(hasParentId(parentId));
    }
}
