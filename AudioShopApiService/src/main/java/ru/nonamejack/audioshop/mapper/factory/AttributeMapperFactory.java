package ru.nonamejack.audioshop.mapper.factory;

import ru.nonamejack.audioshop.dto.attribute.AttributeDto;
import ru.nonamejack.audioshop.dto.request.ProductAttributeDtoRequest;
import ru.nonamejack.audioshop.mapper.strategy.AttributeProductMappingStrategy;
import ru.nonamejack.audioshop.model.Attribute;
import ru.nonamejack.audioshop.model.Product;
import ru.nonamejack.audioshop.model.ProductAttribute;
import org.springframework.stereotype.Component;
import ru.nonamejack.audioshop.service.MessageService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


@Component
public class AttributeMapperFactory {
    private final List<AttributeProductMappingStrategy> strategies;

    private final MessageService messageService;
    public AttributeMapperFactory(List<AttributeProductMappingStrategy> strategies, MessageService messageService) {

        this.strategies = strategies;
        this.messageService = messageService;
    }

    public ProductAttribute createProductAttribute(Product product, Attribute attribute, ProductAttributeDtoRequest request) {
        return strategies.stream()
                .filter(strategies -> strategies.supports(attribute.getValueType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No mapper for " + attribute.getValueType()))
                .toProductAttribute(product, attribute, request);
    }

    public void updateProductAttribute(ProductAttribute productAttribute, ProductAttributeDtoRequest request){
        strategies.stream()
                .filter(strategies -> strategies.supports(productAttribute.getAttribute().getValueType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No mapper for " + productAttribute.getAttribute().getValueType()))
                .setAttributeValue(productAttribute, request);
    }

    public AttributeDto mapAttribute(ProductAttribute productAttribute, boolean required){
        return strategies.stream()
                .filter(strategies -> strategies.supports(productAttribute.getAttribute().getValueType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        messageService.getMessage("error.attribute.no_mapper",
                                productAttribute.getAttribute().getValueType())
                ))
                .toDto(productAttribute, required);
    }

    public List<AttributeDto> mapAttributes(List<ProductAttribute> list,  Set<Integer> requiredAttributeIds) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .sorted(Comparator
                        .comparing((ProductAttribute pa) -> !requiredAttributeIds.contains(pa.getAttribute().getAttributeId())) // обязательные — первыми
                        .thenComparing((ProductAttribute pa) -> pa.getAttribute().getValueType())
                        .thenComparing(pa -> pa.getAttribute().getName(), String.CASE_INSENSITIVE_ORDER))
                .map(pa -> {
                    boolean isRequired = requiredAttributeIds.contains(pa.getAttribute().getAttributeId());
                    return mapAttribute(pa, isRequired);
                })
                .toList();
    }
}
