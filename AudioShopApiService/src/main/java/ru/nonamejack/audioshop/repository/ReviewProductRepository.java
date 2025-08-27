package ru.nonamejack.audioshop.repository;

import ru.nonamejack.audioshop.model.ReviewProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewProductRepository extends JpaRepository<ReviewProduct, Integer> {
}
