package ru.nonamejack.audioshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nonamejack.audioshop.model.CategoryAttribute;
import ru.nonamejack.audioshop.model.CategoryAttributeKey;

@Repository
public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute, CategoryAttributeKey> {

}
