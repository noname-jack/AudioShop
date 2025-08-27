package ru.nonamejack.audioshop.mapper.strategy;

import ru.nonamejack.audioshop.dto.attribute.AttributeDto;
import ru.nonamejack.audioshop.dto.request.ProductAttributeDtoRequest;
import ru.nonamejack.audioshop.model.Attribute;
import ru.nonamejack.audioshop.model.Product;
import ru.nonamejack.audioshop.model.ProductAttribute;
import ru.nonamejack.audioshop.model.enums.ValueType;

public interface AttributeProductMappingStrategy {
    boolean supports(ValueType valueType);

    AttributeDto toDto(ProductAttribute attribute, boolean required);
    ProductAttribute toProductAttribute(Product product, Attribute attribute, ProductAttributeDtoRequest request);

    void setAttributeValue(ProductAttribute productAttribute, ProductAttributeDtoRequest request);

    void validateValue(ProductAttributeDtoRequest request);
}
