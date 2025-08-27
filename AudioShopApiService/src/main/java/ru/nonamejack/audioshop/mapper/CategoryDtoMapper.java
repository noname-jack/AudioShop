package ru.nonamejack.audioshop.mapper;

import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import ru.nonamejack.audioshop.dto.category.CategoryDto;
import ru.nonamejack.audioshop.dto.category.CategoryTreeDto;
import ru.nonamejack.audioshop.dto.request.CategoryRequest;
import ru.nonamejack.audioshop.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.nonamejack.audioshop.service.ImageService;

@Mapper(componentModel = "spring")
public abstract class CategoryDtoMapper {
    @Autowired
    protected ImageService imageService;

    @Mapping(target = "imgPath", expression = "java(imageService.buildImageUrl(category.getImgPath()))")
    public  abstract  CategoryDto categoryToCategoryDTO(Category category);

    @Mapping(target = "imgPath", expression = "java(imageService.buildImageUrl(category.getImgPath()))")

    @Mapping(target = "children", ignore = true)
    public  abstract  CategoryTreeDto categoryToCategoryTreeDTO(Category category);

    public Page<CategoryDto> toDtoPage(Page<Category> categoryPage) {
        return categoryPage.map(this::categoryToCategoryDTO);
    }

    @Mapping(target = "imgPath", ignore = true)
    @Mapping(target = "categoryAttributeList", ignore = true)
    @Mapping(target = "categoryId", ignore = true)
    public abstract Category toEntity(CategoryRequest categoryDto);

    @Mapping(target = "imgPath", ignore = true)
    @Mapping(target = "categoryAttributeList", ignore = true)
    @Mapping(target = "categoryId", ignore = true)
    public abstract void updateCategory(CategoryRequest categoryDto, @MappingTarget Category category);
}
