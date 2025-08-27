package ru.nonamejack.audioshop.repository;

import ru.nonamejack.audioshop.dto.brand.BrandMiniDto;
import ru.nonamejack.audioshop.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    @Query("select distinct p.brand from Product p "
            + "where p.category.categoryId = :cid "
            + "and p.active = true")
    List<Brand> findDistinctBrands(@Param("cid") Integer categoryId);


    List<BrandMiniDto> findAllBy();

    Optional<Brand> findByName(String name);

    List<Brand> findByNameContainingIgnoreCase(String name);

}
