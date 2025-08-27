package ru.nonamejack.audioshop.mapper;

import ru.nonamejack.audioshop.dto.attribute.*;
import ru.nonamejack.audioshop.dto.request.AttributeDtoRequest;
import ru.nonamejack.audioshop.model.*;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AttributeMapper {

    @Mapping(source = "name",        target = "attributeName")
    AttributeDto toDto(Attribute attribute);
    List<AttributeDto> toDtoList(List<Attribute> attributeList);

    @Mapping(target = "attributeId", ignore = true)
    @Mapping(target = "filterType", source = "filterType")
    Attribute toEntity(AttributeDtoRequest dto);

    void updateEntity(AttributeDtoRequest dto, @MappingTarget Attribute attribute);

    // === AttributeDto ===
    @Mapping(source = "pa.attribute.attributeId", target = "attributeId")
    @Mapping(source = "pa.attribute.name",        target = "attributeName")
    @Mapping(source = "pa.attribute.valueType", target = "valueType")
    @Mapping(source = "pa.attribute.filterType", target = "filterType")
    AttributeDto toBaseDto(ProductAttribute pa, boolean required);

    @InheritConfiguration(name = "toBaseDto")
    @Mapping(source = "required", target = "required")
    AttributeProductDto toProductAttributeDto(ProductAttribute pa, boolean required);

    // === DoubleAttributeDto ===
    @InheritConfiguration(name = "toProductAttributeDto")
    @Mapping(source = "pa.doubleValue",             target = "value")
    DoubleAttributeDto toDoubleDto(ProductAttribute pa, boolean required);

    // === StringAttributeDto ===
    @InheritConfiguration(name = "toProductAttributeDto")
    @Mapping(source = "pa.stringValue",          target = "value")
    StringAttributeDto toStringDto(ProductAttribute pa, boolean required);

    // === BooleanAttributeDto ===
    @InheritConfiguration(name = "toProductAttributeDto")
    @Mapping(source = "pa.booleanValue",         target = "value")
    BooleanAttributeDto toBooleanDto(ProductAttribute pa, boolean required);


    @Mapping(target = "id", expression = "java(createKey(product, attribute))")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "attribute", source = "attribute")
    @Mapping(source = "value", target = "booleanValue")
    @Mapping(target = "doubleValue", ignore = true)
    @Mapping(target = "stringValue", ignore = true)
    ProductAttribute toBooleanProductAttribute(Product product, Attribute attribute, Boolean value);

    @Mapping(target = "id", expression = "java(createKey(product, attribute))")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "attribute", source = "attribute")
    @Mapping(source = "value", target = "doubleValue")
    @Mapping(target = "stringValue", ignore = true)
    @Mapping(target = "booleanValue", ignore = true)
    ProductAttribute toDoubleProductAttribute(Product product, Attribute attribute, Double value);

    @Mapping(target = "id", expression = "java(createKey(product, attribute))")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "attribute", source = "attribute")
    @Mapping(source = "value", target = "stringValue")
    @Mapping(target = "doubleValue", ignore = true)
    @Mapping(target = "booleanValue", ignore = true)
    ProductAttribute toStringProductAttribute(Product product, Attribute attribute, String value);

    @Mapping(source = "value", target = "doubleValue")
    @Mapping(target = "stringValue", ignore = true)
    @Mapping(target = "booleanValue", ignore = true)
    void updateDoubleProductAttribute(@MappingTarget ProductAttribute pa, Double value);

    @Mapping(source = "value", target = "stringValue")
    @Mapping(target = "doubleValue", ignore = true)
    @Mapping(target = "booleanValue", ignore = true)
    void updateStringProductAttribute(@MappingTarget ProductAttribute pa, String value);
    @Mapping(source = "value", target = "booleanValue")
    @Mapping(target = "doubleValue", ignore = true)
    @Mapping(target = "stringValue", ignore = true)
    void updateBooleanProductAttribute(@MappingTarget ProductAttribute pa, Boolean value);

    @Mapping(source = "attribute.attributeId", target = "attributeId")
    @Mapping(source = "attribute.name",        target = "attributeName")
    @Mapping(source = "attribute.valueType", target = "valueType")
    @Mapping(source = "attribute.filterType", target = "filterType")
    @Mapping(source = "required", target = "required")
    @Mapping(source = "useFilter", target = "useFilter")
    AttributeCategoryDto toAttributeCategory(CategoryAttribute categoryAttribute);

    List<AttributeCategoryDto> toAttributeCategoryList(List<CategoryAttribute> categoryAttributeList);

    @Mapping(target = "availableValuesString", ignore = true)
    StringListAttributeFilterDto toStringFilter(AttributeCategoryDto base);

    @Mapping(target = "availableValuesDouble", ignore = true)
    DoubleListAttributeFilterDto toDoubleListFilter(AttributeCategoryDto base);

    @Mapping(target = "minValue", ignore = true)
    @Mapping(target = "maxValue", ignore = true)
    DoubleRangeAttributeDto toRangeFilter(AttributeCategoryDto base);

    @Mapping(target = "availableValuesBoolean", ignore = true)
    BooleanListAttributeDto toBooleanListFilter(AttributeCategoryDto base);


    default ProductAttributeKey createKey(Product product, Attribute attribute) {
        return new ProductAttributeKey(product.getProductId(), attribute.getAttributeId());
    }

    default ProductAttributeKey createKey(Integer productId, Integer attributeId) {
        return new ProductAttributeKey(productId, attributeId);
    }
}
