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
import ru.nonamejack.audioshop.service.MessageService;


@Component
public class DoubleAttributeProductMappingStrategy implements AttributeProductMappingStrategy {
    private final AttributeMapper attributeMapper;
    private final MessageService messageService;

    public DoubleAttributeProductMappingStrategy(AttributeMapper attributeMapper, MessageService messageService) {
        this.attributeMapper = attributeMapper;
        this.messageService = messageService;
    }

    @Override
    public boolean supports(ValueType valueType) {
        return  valueType.equals(ValueType.DOUBLE);
    }

    @Override
    public AttributeDto toDto(ProductAttribute attribute,boolean required) {
        return attributeMapper.toDoubleDto(attribute, required);
    }

    @Override
    public ProductAttribute toProductAttribute(Product product, Attribute attribute, ProductAttributeDtoRequest request) {
        validateValue(request);
        return attributeMapper.toDoubleProductAttribute(product, attribute, request.doubleValue());
    }

    @Override
    public void setAttributeValue(ProductAttribute productAttribute, ProductAttributeDtoRequest request) {
        validateValue(request);
        attributeMapper.updateDoubleProductAttribute(productAttribute, request.doubleValue());
    }

    @Override
    public void validateValue(ProductAttributeDtoRequest request) {
        if (request.doubleValue() == null) {
            throw new ResourceCreationException(messageService.getMessage("error.attribute.number_value_empty"));
        }
    }


}
