package ru.nonamejack.audioshop.controller.admin;

import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nonamejack.audioshop.dto.ApiResponse;
import ru.nonamejack.audioshop.service.ProductImageService;
import ru.nonamejack.audioshop.service.ProductService;

@RestController
@RequestMapping("/admin/products/images")
public class AdminProductImageController {

    private final ProductImageService productImageService;

    public AdminProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @DeleteMapping("/{imageId}")
    public ApiResponse<Boolean> deleteImageToProduct(@PathVariable  @Positive Integer imageId){
        productImageService.deleteImageToProduct(imageId);
        return ApiResponse.success(true);
    }
}
