package ru.nonamejack.audioshop.controller;

import ru.nonamejack.audioshop.dto.ApiResponse;
import ru.nonamejack.audioshop.dto.product.ProductDetailsDto;
import ru.nonamejack.audioshop.dto.product.ProductSummaryDto;
import ru.nonamejack.audioshop.dto.request.ProductFilterRequest;
import ru.nonamejack.audioshop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Products", description = "Поиск и получение информации о товарах")
@RestController
@RequestMapping("/catalog/products")
@Validated
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping()
    public ApiResponse<PagedModel<ProductSummaryDto>> getAllProducts(
            @Valid @RequestBody ProductFilterRequest filterRequest,
            @PageableDefault(size = 20, sort = "price") Pageable pageable
    ) {
        Page<ProductSummaryDto> page = productService.findProducts(filterRequest, pageable);
        return ApiResponse.success(new PagedModel<>(page));
    }


    @Operation(summary = "Краткая информация о товаре", description = "Возвращает основные данные по ID товара")
    @GetMapping("/summary/{id}")
    public ApiResponse<ProductSummaryDto> getProductSummaryById(@PathVariable Integer id) {
        return ApiResponse.success(productService.getProductSummary(id));
    }

    @Operation(summary = "Полные детали товара", description = "Возвращает развернутую информацию по ID товара")
    @GetMapping("/detailed/{id}")
    public ApiResponse<ProductDetailsDto> getProductDetailedById(@PathVariable Integer id) {
        return ApiResponse.success(productService.getProductDetails(id));
    }


}
