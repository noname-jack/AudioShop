package ru.nonamejack.audioshop.repository;

import ru.nonamejack.audioshop.model.ProductAttribute;
import ru.nonamejack.audioshop.model.ProductAttributeKey;
import ru.nonamejack.audioshop.util.IRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, ProductAttributeKey> {
    @Query("select distinct pa.stringValue from ProductAttribute pa "
            + "where pa.attribute.attributeId = :aid "
            + "and pa.product.category.categoryId = :cid "
            + "and pa.product.active = true "
            + "and pa.stringValue is not null")
    List<String> findDistinctStringValues(@Param("cid") Integer cid, @Param("aid") Integer attributeId);

    @Query("select distinct pa.doubleValue from ProductAttribute pa "
            + "where pa.attribute.attributeId = :aid "
            + "and pa.product.category.categoryId = :cid "
            + "and pa.product.active = true "
            + "and pa.doubleValue is not null")
    List<Double> findDistinctDoubleValues(@Param("cid") Integer cid, @Param("aid") Integer attributeId);

    @Query("select min(pa.doubleValue) as minValue, max(pa.doubleValue) as maxValue from ProductAttribute pa "
            + "where pa.attribute.attributeId = :aid "
            + "and pa.product.category.categoryId = :cid "
            + "and pa.product.active = true "
            + "and pa.doubleValue is not null")
    IRange findRange(@Param("cid") Integer cid, @Param("aid") Integer attributeId);

    @Query("select distinct pa.booleanValue from ProductAttribute pa "
            + "where pa.attribute.attributeId = :aid "
            + "and pa.product.category.categoryId = :cid "
            + "and pa.product.active = true "
            + "and pa.booleanValue is not null")
    List<Boolean> findDistinctBooleanValues(@Param("cid") Integer cid, @Param("aid") Integer attributeId);


    List<ProductAttribute> findAllByProductProductId(Integer productId);

}
