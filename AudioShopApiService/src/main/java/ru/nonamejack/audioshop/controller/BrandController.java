package ru.nonamejack.audioshop.controller;

import ru.nonamejack.audioshop.dto.ApiResponse;
import ru.nonamejack.audioshop.dto.brand.BrandDto;
import ru.nonamejack.audioshop.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Brands", description = "Управление брендами товара")
@RestController
@RequestMapping("/catalog/brands")
public class BrandController {

    private final BrandService brandService;
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @Operation(summary = "Список всех брендов", description = "Возвращает полный список доступных брендов")
    @GetMapping("/all")
    public ApiResponse<List<BrandDto>> getAllBrands() {
        return ApiResponse.success(brandService.getAllBrands());
    }

    @Operation(summary = "Детали бренда", description = "Возвращает данные по бренду по его ID")
    @GetMapping("/{id}")
    public ApiResponse<BrandDto> getBrandById(@PathVariable Integer id) {
        return ApiResponse.success(brandService.getBrandById(id));
    }
}
