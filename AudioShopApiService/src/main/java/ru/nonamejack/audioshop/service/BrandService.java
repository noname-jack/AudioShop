package ru.nonamejack.audioshop.service;

import org.springframework.web.multipart.MultipartFile;
import ru.nonamejack.audioshop.dto.brand.BrandDto;
import ru.nonamejack.audioshop.dto.brand.BrandMiniDto;
import ru.nonamejack.audioshop.dto.request.BrandDtoRequest;
import ru.nonamejack.audioshop.exception.custom.NotFoundException;
import ru.nonamejack.audioshop.exception.custom.ResourceCreationException;
import ru.nonamejack.audioshop.mapper.BrandMapper;
import ru.nonamejack.audioshop.model.Brand;
import ru.nonamejack.audioshop.repository.BrandRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final MessageService messageService;

    private final ImageService imageService;
    public BrandService(BrandRepository brandRepository, BrandMapper brandMapper, MessageService messageService, ImageService imageService) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
        this.messageService = messageService;
        this.imageService = imageService;
    }

    public List<BrandDto> getAllBrands(){
        return brandMapper.toBrandDtoList(brandRepository.findAll());
    }

    public List<BrandDto> getAllBrands(String name){
        return brandMapper.toBrandDtoList(brandRepository.findByNameContainingIgnoreCase(name));
    }

    public List<BrandMiniDto> getAllBrandsMini(){
        return brandRepository.findAllBy();
    }

    public BrandDto getBrandById(Integer brandId){
        Brand brand = getBrandEntityById(brandId);
        return brandMapper.toBrandDto(brand);
    }
    public Brand getBrandEntityById(Integer brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new NotFoundException(messageService.getMessage("error.brand.not_found", brandId))
        );
    }


    public BrandDto createBrand(BrandDtoRequest brandDto) {
        if(brandRepository.findByName(brandDto.name()).isPresent()){
            throw new ResourceCreationException(messageService.getMessage("error.brand.already_exists"));
        }
        Brand newBrand = brandMapper.toBrand(brandDto);
        Brand savedBrand = brandRepository.save(newBrand);
        return brandMapper.toBrandDto(savedBrand);
    }

    public void deleteBrandById(Integer id) {
        brandRepository.deleteById(id);
    }

    public void updateBrandById(Integer id, BrandDtoRequest brandDto) {
        Brand brand = getBrandEntityById(id);

        Optional<Brand> brandWithSameName = brandRepository.findByName(brandDto.name());
        if (brandWithSameName.isPresent() && !brandWithSameName.get().getId().equals(id)) {
            throw new IllegalArgumentException(messageService.getMessage("error.brand.already_exists"));
        }

        brand.setName(brandDto.name());
        brand.setShortDescription(brandDto.shortDescription());
        brand.setDescription(brandDto.description());
        brandRepository.save(brand);
    }

    public void addImageToBrand(MultipartFile file, Integer id){
        Brand brand = getBrandEntityById(id);

        imageService.saveImageAndAssign(
                file,
                "products",
                imagePath -> {
                    brand.setImagePath(imagePath);
                    return brand;
                },
                brandRepository::save
        );
    }
}
