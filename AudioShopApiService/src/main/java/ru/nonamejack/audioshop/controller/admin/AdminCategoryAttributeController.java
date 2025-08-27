package ru.nonamejack.audioshop.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.nonamejack.audioshop.dto.ApiResponse;
import ru.nonamejack.audioshop.dto.attribute.AttributeCategoryDto;
import ru.nonamejack.audioshop.dto.request.CategoryAttributeDtoRequest;
import ru.nonamejack.audioshop.service.CategoryAttributeService;

import java.util.List;

@RestController
@RequestMapping("/admin/categories/{categoryId}/attributes")
@Validated
public class AdminCategoryAttributeController {

    private final CategoryAttributeService categoryAttributeService;

    public AdminCategoryAttributeController(CategoryAttributeService categoryAttributeService) {
        this.categoryAttributeService = categoryAttributeService;
    }

    @GetMapping()
    public ApiResponse<List<AttributeCategoryDto>> getAllCategoryAttributes(@PathVariable Integer categoryId) {
        return ApiResponse.success(categoryAttributeService.getCategoryAttribute(categoryId));
    }

    @PutMapping("/{attributeId}")
    public ApiResponse<Boolean> setAttributeCategory(@PathVariable  @Positive Integer categoryId,
                                                     @PathVariable @Positive Integer attributeId,
                                                     @RequestBody @Valid CategoryAttributeDtoRequest attributeCategoryDto) {
        categoryAttributeService.setCategoryAttribute(categoryId, attributeId, attributeCategoryDto);
        return ApiResponse.success(true);
    }

    @DeleteMapping("/{attributeId}")
    public ApiResponse<Boolean> deleteCategoryAttribute(
            @PathVariable  @Positive Integer categoryId,
            @PathVariable  @Positive Integer attributeId) {

        categoryAttributeService.deleteCategoryAttribute(categoryId, attributeId);
        return ApiResponse.success(true);
    }

}
