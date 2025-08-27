package ru.nonamejack.audioshop.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.nonamejack.audioshop.dto.ApiResponse;
import ru.nonamejack.audioshop.dto.attribute.AttributeDto;
import ru.nonamejack.audioshop.dto.request.ProductAttributeDtoRequest;
import ru.nonamejack.audioshop.service.ProductAttributeService;

import java.util.List;

@RestController
@RequestMapping("/admin/product/{productId}/attributes")
@Validated
public class AdminProductAttributeController {

    private  final ProductAttributeService productAttributeService;

    public AdminProductAttributeController(ProductAttributeService productAttributeService) {
        this.productAttributeService = productAttributeService;
    }

    @GetMapping()
    public ApiResponse<List<AttributeDto>> getAllProductAttributes(@PathVariable Integer productId) {
        return ApiResponse.success(productAttributeService.getProductAttributes(productId));
    }

    @PutMapping({"/{attributeId}"})
    public ApiResponse<Boolean> setProductAttribute(@PathVariable  @Positive Integer productId,
            @PathVariable  @Positive Integer attributeId,
            @RequestBody @NotNull @Valid ProductAttributeDtoRequest productAttributeDtoRequest) {
        productAttributeService.setProductAttribute(productId, attributeId, productAttributeDtoRequest);
        return ApiResponse.success(true);
    }
    @DeleteMapping("/{attributeId}")
    public ApiResponse<Boolean> deleteAttribute(@PathVariable  @Positive Integer productId,
                                                @PathVariable @Positive Integer attributeId){
        productAttributeService.deleteAttribute(attributeId, productId);
        return  ApiResponse.success(true);
    }

}
