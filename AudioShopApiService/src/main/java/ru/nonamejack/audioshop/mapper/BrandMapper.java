package ru.nonamejack.audioshop.mapper;


import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nonamejack.audioshop.dto.brand.BrandDto;
import ru.nonamejack.audioshop.dto.request.BrandDtoRequest;
import ru.nonamejack.audioshop.model.Brand;
import org.mapstruct.Mapper;
import ru.nonamejack.audioshop.service.ImageService;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BrandMapper {
    @Autowired
    protected ImageService imageService;
    @Mapping(target = "imagePath", expression = "java(imageService.buildImageUrl(brand.getImagePath()))")
    public  abstract BrandDto toBrandDto(Brand brand);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagePath", ignore = true)
    public  abstract Brand toBrand(BrandDtoRequest brandDto);

    public  abstract List<BrandDto> toBrandDtoList(List<Brand> brands);
}
