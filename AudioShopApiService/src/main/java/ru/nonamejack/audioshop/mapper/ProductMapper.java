package ru.nonamejack.audioshop.mapper;

import ru.nonamejack.audioshop.dto.attribute.AttributeDto;
import ru.nonamejack.audioshop.dto.product.AdminProductSummaryDto;
import ru.nonamejack.audioshop.dto.product.ProductDetailsDto;
import ru.nonamejack.audioshop.dto.product.ProductSummaryDto;
import ru.nonamejack.audioshop.dto.product.ReviewDto;
import ru.nonamejack.audioshop.dto.request.ProductDtoRequest;
import ru.nonamejack.audioshop.mapper.factory.AttributeMapperFactory;
import ru.nonamejack.audioshop.model.*;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nonamejack.audioshop.service.BrandService;
import ru.nonamejack.audioshop.service.CategoryService;
import ru.nonamejack.audioshop.service.ImageService;

import java.util.*;
import java.util.stream.Collectors;


@Mapper(componentModel="spring")
public abstract class ProductMapper {

    @Autowired
    protected AttributeMapperFactory attributeMapperFactory;
    @Autowired
    protected ImageService imageService;
    @Autowired
    protected BrandService brandService;
    @Autowired
    protected CategoryService categoryService;




    @Mapping(target = "brandName", source = "brand.name")
    @Mapping(target = "mainImage", expression = "java(imageService.buildImageUrl(product.getMainImage()))")
    public abstract ProductSummaryDto toSummary(Product product);

    @Mapping(target = "brandName", source = "brand.name")
    @Mapping(target = "mainImage", expression = "java(imageService.buildImageUrl(product.getMainImage()))")
    public abstract AdminProductSummaryDto toAdminProductSummaryDto(Product product);


    @Mapping(target = "brandName", source = "brand.name")
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "imagesMap", expression = "java(mapProductImages(product.getProductImages()))")
    @Mapping(target = "reviews", source = "productReviewList")
    @Mapping(target = "attributes", source = "product",  qualifiedByName = "mapAttributesProduct")
    @Mapping(target = "mainImage", expression = "java(imageService.buildImageUrl(product.getMainImage()))")
    public  abstract ProductDetailsDto toDetails(Product product);


    @Mapping(target = "brand", expression = "java(brandService.getBrandEntityById(productDtoRequest.brandId()))")
    @Mapping(target = "category", expression = "java(categoryService.getCategoryEntityById(productDtoRequest.categoryId()))")
    @Mapping(target = "productId", ignore = true) // если id есть
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "productAttributeList", ignore = true)
    @Mapping(target = "productReviewList", ignore = true)
    @Mapping(target = "mainImage", ignore = true)
    public abstract Product toProduct(ProductDtoRequest productDtoRequest);


    @Mapping(target = "brand", expression = "java(brandService.getBrandEntityById(dto.brandId()))")
    @Mapping(target = "category", expression = "java(categoryService.getCategoryEntityById(dto.categoryId()))")
    @Mapping(target = "productId", ignore = true) // если id есть
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "productAttributeList", ignore = true)
    @Mapping(target = "productReviewList", ignore = true)
    @Mapping(target = "mainImage", ignore = true)
    public abstract void updateProductFromDto(ProductDtoRequest dto, @MappingTarget Product product);


    @Named("mapAttributesProduct")
    public List<AttributeDto> mapAllAttributes(Product product) {
        if (product == null
                || product.getCategory() == null
                || product.getCategory().getCategoryAttributeList() == null) {
            return Collections.emptyList();
        }
        // Собираем ID атрибутов, у которых isRequired == true
        Set<Integer> requiredIds = product.getCategory()
                .getCategoryAttributeList().stream()
                .filter(CategoryAttribute::isRequired)
                .map(ca -> ca.getAttribute().getAttributeId())
                .collect(Collectors.toSet());
        return attributeMapperFactory.mapAttributes(product.getProductAttributeList(), requiredIds);
    }


    protected Map<Integer, String> mapProductImages(List<ProductImage> images) {
        if (images == null) {
            return Collections.emptyMap();
        }
        return images.stream()
                .collect(Collectors.toMap(
                        ProductImage::getImageId,
                        img -> imageService.buildImageUrl(img.getImageUrl())
                ));
    }

}
