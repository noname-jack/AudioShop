package ru.nonamejack.audioshop.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import ru.nonamejack.audioshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nonamejack.audioshop.util.PriceRange;

import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository< Product, Integer>, JpaSpecificationExecutor<Product> {


    @Query("select min(p.price) as minValue, max(p.price) as maxValue from Product p "
            + "where p.category.categoryId = :cid "
            + "and p.active = true")
    PriceRange findPriceRange(@Param("cid") Integer categoryId);


    @NotNull
    @EntityGraph(attributePaths = {"brand"})
    Page<Product> findAll(@NotNull Specification<Product> spec, @NotNull Pageable pageable);



    boolean existsByCategoryCategoryId(Integer categoryId);
}
