package ru.nonamejack.audioshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.nonamejack.audioshop.dto.category.CategoryMiniDto;
import ru.nonamejack.audioshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    List<Category> findByParentCategoryId(Integer parentId);

    @Query("""
        SELECT c.categoryId AS categoryId, c.name AS name
        FROM Category c
        WHERE NOT EXISTS (
            SELECT 1 FROM Category sub
            WHERE sub.parentCategoryId = c.categoryId
        )
    """)
    List<CategoryMiniDto> findLeafCategories();
} 