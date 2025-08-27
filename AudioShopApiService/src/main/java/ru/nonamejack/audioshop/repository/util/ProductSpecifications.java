package ru.nonamejack.audioshop.repository.util;

import org.springframework.util.CollectionUtils;
import ru.nonamejack.audioshop.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public class ProductSpecifications {

    public static Specification<Product> byCategory(Integer categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) {
                return cb.conjunction(); // всегда true, фильтр не применяется
            }
            return cb.equal(root.get("category").get("categoryId"), categoryId);
        };
    }

    public static Specification<Product> byBrands(List<Integer> selectedBrandIds) {
        return (root, query, cb) -> {
            if (CollectionUtils.isEmpty(selectedBrandIds)) {
                return cb.conjunction();
            }
            return root.get("brand").get("id").in(selectedBrandIds);
        };
    }

    // Отдельные простые спецификации:
    public static Specification<Product> byMinPrice(BigDecimal minPrice) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> byMaxPrice(BigDecimal maxPrice) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("active"), active);
        };
    }

    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

}


