package ru.nonamejack.audioshop.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nonamejack.audioshop.dto.ApiResponse;
import ru.nonamejack.audioshop.dto.category.CategoryDto;
import ru.nonamejack.audioshop.dto.category.CategoryMiniDto;
import ru.nonamejack.audioshop.dto.request.CategoryRequest;
import ru.nonamejack.audioshop.service.CategoryService;

import java.util.List;


@RestController
@RequestMapping("/admin/categories")
@Validated
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ApiResponse<PagedModel<CategoryDto>> getCategories(@RequestParam(required = false) String name,
                                                              @RequestParam(required = false) Integer parentId,
                                                              @PageableDefault(size = 20, sort = "categoryId") Pageable pageable) {
        return ApiResponse.success(new PagedModel<>(categoryService.getAllCategory(name, parentId, pageable)));
    }

    @GetMapping("/mini")
    public ApiResponse<List<CategoryMiniDto>> getAllCategoryMini() {
        List<CategoryMiniDto> categories = categoryService.getAllCategoryMini();
        return ApiResponse.success(categories);
    }

    @GetMapping("/{categoryId}")
    public ApiResponse<CategoryDto> getCategoryById(@PathVariable  @Positive Integer categoryId) {
        return ApiResponse.success(categoryService.getCategoryById(categoryId));
    }

    @PostMapping
    public ApiResponse<CategoryDto> createCategory(@RequestBody @Valid CategoryRequest categoryDto) {
        return ApiResponse.success(categoryService.createCategory(categoryDto));
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<Boolean> deleteCategoryById(@PathVariable  @Positive Integer categoryId){
        categoryService.deleteCategory(categoryId);
        return ApiResponse.success(true);
    }

    @PatchMapping("/{categoryId}/image")
    public ApiResponse<Boolean> addMainImageToCategory(@PathVariable  @Positive Integer categoryId,
                                                       @RequestParam MultipartFile file){
        categoryService.addImageToCategory(categoryId, file);
        return ApiResponse.success(true);
    }

    @PutMapping("/{categoryId}")
    public ApiResponse<Boolean> updateCategory(@PathVariable  @Positive Integer categoryId,
                                               @RequestBody @Valid CategoryRequest categoryDto){
        categoryService.updateCategory(categoryId, categoryDto);
        return ApiResponse.success(true);
    }
}
