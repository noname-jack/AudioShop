package ru.nonamejack.audioshop.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nonamejack.audioshop.dto.ApiResponse;
import ru.nonamejack.audioshop.dto.brand.BrandDto;
import ru.nonamejack.audioshop.dto.brand.BrandMiniDto;
import ru.nonamejack.audioshop.dto.request.BrandDtoRequest;
import ru.nonamejack.audioshop.service.BrandService;

import java.util.List;

@RestController
@RequestMapping("/admin/brands")
@Validated
public class AdminBrandController {

    private final BrandService brandService;

    public AdminBrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping()
    public ApiResponse<List<BrandDto>> getAllBrands(@RequestParam(required = false)  String name){
        if (name == null || name.isBlank()) {
            return ApiResponse.success(brandService.getAllBrands());
        }
        return ApiResponse.success(brandService.getAllBrands(name));
    }


    @GetMapping("/mini")
    public ApiResponse<List<BrandMiniDto>> getAllBrandsMini(){
        return ApiResponse.success(brandService.getAllBrandsMini());
    }

    @GetMapping("/{id}")
    public ApiResponse<BrandDto> getBrandById(@PathVariable @Positive Integer id) {
        return ApiResponse.success(brandService.getBrandById(id));
    }
    @PostMapping()
    public ApiResponse<BrandDto> createBrand(@RequestBody @Valid BrandDtoRequest brandDto) {
        return ApiResponse.success(brandService.createBrand(brandDto));
    }

    @PutMapping("/{id}")
    public ApiResponse<Boolean> updateBrandById(@PathVariable  @Positive  Integer id, @RequestBody @Valid BrandDtoRequest brandDto){
        brandService.updateBrandById(id, brandDto);
        return ApiResponse.success(true);
    }

    @PatchMapping("/{id}/image")
    public ApiResponse<Boolean> addImageToBrand(@PathVariable  @Positive  Integer id,@RequestParam MultipartFile file){
        brandService.addImageToBrand(file, id);;
        return ApiResponse.success(true);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteBrandById(@PathVariable  @Positive Integer id){
        brandService.deleteBrandById(id);
        return ApiResponse.success(true);
    }



}
