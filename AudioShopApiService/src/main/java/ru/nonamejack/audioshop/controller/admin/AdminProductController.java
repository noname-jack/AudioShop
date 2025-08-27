package ru.nonamejack.audioshop.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nonamejack.audioshop.dto.ApiResponse;
import ru.nonamejack.audioshop.dto.product.AdminProductSummaryDto;
import ru.nonamejack.audioshop.dto.product.ProductDetailsDto;
import ru.nonamejack.audioshop.dto.request.ProductDtoRequest;
import ru.nonamejack.audioshop.dto.request.ProductFilterAdminRequest;
import ru.nonamejack.audioshop.service.ProductService;

@RestController
@RequestMapping("/admin/products")
@Validated
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ApiResponse<PagedModel<AdminProductSummaryDto>> getAllProducts(
            ProductFilterAdminRequest productFilterAdminRequest,
            @PageableDefault(size = 20, sort = "price") Pageable pageable){
        return ApiResponse.success(new PagedModel<>
                (productService.getAllProductsWithAdmin(productFilterAdminRequest,pageable)));
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductDetailsDto> getProductDetailedById(@PathVariable  @Positive Integer productId) {
        return ApiResponse.success(productService.getProductDetails(productId));
    }
    @PostMapping()
    public ApiResponse<ProductDetailsDto> createProduct(@RequestBody @Valid ProductDtoRequest productDtoRequest){
        return ApiResponse.success(productService.createProduct(productDtoRequest));
    }
    @PutMapping("/{productId}")
    public ApiResponse<Boolean> updateProductById(@PathVariable  @Positive Integer productId, @RequestBody @Valid ProductDtoRequest productDtoRequest){
        productService.updateProductById(productId,productDtoRequest);
        return ApiResponse.success(true);
    }


    @DeleteMapping("/{productId}")
    public ApiResponse<Boolean> deleteProductById(@PathVariable  @Positive Integer productId){
        productService.deleteProductById(productId);
        return ApiResponse.success(true);
    }

    @PatchMapping("/{productId}/active")
    public ApiResponse<Boolean> updateProductActiveById(@PathVariable  @Positive Integer productId, @RequestBody boolean active){
        productService.activeProductById(productId, active);
        return ApiResponse.success(true);
    }

    @PatchMapping("/{productId}/image")
    public ApiResponse<Boolean> addMainImageToProduct(@PathVariable  @Positive Integer productId,@RequestParam MultipartFile file){
        productService.addImageToProduct(productId, file);
        return ApiResponse.success(true);
    }


}
