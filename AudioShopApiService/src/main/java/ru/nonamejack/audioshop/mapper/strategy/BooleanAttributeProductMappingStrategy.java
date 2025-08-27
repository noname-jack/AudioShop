package ru.nonamejack.audioshop.mapper.strategy;

import ru.nonamejack.audioshop.dto.attribute.AttributeDto;
import ru.nonamejack.audioshop.dto.request.ProductAttributeDtoRequest;
import ru.nonamejack.audioshop.exception.custom.ResourceCreationException;
import ru.nonamejack.audioshop.mapper.AttributeMapper;
import ru.nonamejack.audioshop.model.Attribute;
import ru.nonamejack.audioshop.model.Product;
import ru.nonamejack.audioshop.model.ProductAttribute;
import ru.nonamejack.audioshop.model.enums.ValueType;
import org.springframework.stereotype.Component;

@Component
public class BooleanAttributeProductMappingStrategy implements AttributeProductMappingStrategy {

    private final AttributeMapper attributeMapper;

    public BooleanAttributeProductMappingStrategy(AttributeMapper attributeMapper) {
        this.attributeMapper = attributeMapper;
    }

    @Override
    public boolean supports(ValueType valueType) {
        return  valueType
                .equals(ValueType.BOOLEAN);
    }

    @Override
    public AttributeDto toDto(ProductAttribute attribute, boolean required) {
        return attributeMapper.toBooleanDto(attribute, required);
    }

    @Override
    public ProductAttribute toProductAttribute(Product product, Attribute attribute, ProductAttributeDtoRequest request) {
        validateValue(request);
        return attributeMapper.toBooleanProductAttribute(product,attribute, request.booleanValue());
    }

    @Override
    public void setAttributeValue(ProductAttribute productAttribute, ProductAttributeDtoRequest request) {
        validateValue(request);
        attributeMapper.updateBooleanProductAttribute(productAttribute, request.booleanValue());
    }

    @Override
    public void validateValue(ProductAttributeDtoRequest request) {
        if (request.booleanValue() == null) {
            throw new ResourceCreationException("Булево значение не может быть пустым");
        }
    }
}
