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
public class StringAttributeProductMappingStrategy implements AttributeProductMappingStrategy {
    private final AttributeMapper attributeMapper;

    public StringAttributeProductMappingStrategy(AttributeMapper attributeMapper) {
        this.attributeMapper = attributeMapper;
    }

    @Override
    public boolean supports(ValueType valueType) {
        return valueType.equals(ValueType.STRING);
    }

    @Override
    public AttributeDto toDto(ProductAttribute attribute, boolean required) {
        return attributeMapper.toStringDto(attribute, required);
    }

    @Override
    public ProductAttribute toProductAttribute(Product product, Attribute attribute, ProductAttributeDtoRequest request) {
        validateValue(request);
        return attributeMapper.toStringProductAttribute(product, attribute, request.stringValue());
    }

    @Override
    public void setAttributeValue(ProductAttribute productAttribute, ProductAttributeDtoRequest request) {
        validateValue(request);
        attributeMapper.updateStringProductAttribute(productAttribute, request.stringValue());
    }

    @Override
    public void validateValue(ProductAttributeDtoRequest request) {
        if (request.stringValue() == null || request.stringValue().trim().isEmpty()) {
            throw new ResourceCreationException("Строковое значение не может быть пустым");
        }
    }


}
