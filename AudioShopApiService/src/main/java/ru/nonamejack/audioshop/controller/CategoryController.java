package ru.nonamejack.audioshop.controller;

import ru.nonamejack.audioshop.dto.ApiResponse;
import ru.nonamejack.audioshop.dto.category.CategoryDto;
import ru.nonamejack.audioshop.dto.category.CategoryTreeDto;
import ru.nonamejack.audioshop.dto.category.FilterOptionDto;
import ru.nonamejack.audioshop.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Categories", description = "Управление категориями товаров")
@RestController
@RequestMapping("/catalog/categories")
public class CategoryController {

    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Дерево категорий", description = "Возвращает все категории в виде дерева")
    @GetMapping("/tree")
    public ApiResponse<List<CategoryTreeDto>> getCategoryTree() {
        return ApiResponse.success(categoryService.getCategoryTree());
    }

    @Operation(summary = "Поддерево категории", description = "Возвращает дерево категории по ID")
    @GetMapping("/tree/{id}")
    public ApiResponse<CategoryTreeDto> getCategoryTreeById(@PathVariable Integer id) {
        return ApiResponse.success(categoryService.getCategoryTreeById(id));
    }

    @Operation(summary = "Данные по категории", description = "Возвращает данные категории по ID")
    @GetMapping("/{id}")
    public ApiResponse<CategoryDto> getCategoryById(@PathVariable Integer id) {
        return ApiResponse.success(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Опции фильтрации", description = "Возвращает параметры фильтрации товаров для категории")
    @GetMapping("/filter/{categoryId}")
    public ApiResponse<FilterOptionDto> getFilterOptionToCategory(@PathVariable Integer categoryId) {
        return ApiResponse.success(categoryService.getFilterOptionToCategory(categoryId));
    }
}
