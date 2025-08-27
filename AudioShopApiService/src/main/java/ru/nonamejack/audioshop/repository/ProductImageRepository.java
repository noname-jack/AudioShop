package ru.nonamejack.audioshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nonamejack.audioshop.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
}
