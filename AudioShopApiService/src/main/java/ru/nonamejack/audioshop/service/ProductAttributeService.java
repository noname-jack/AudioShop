package ru.nonamejack.audioshop.service;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nonamejack.audioshop.dto.attribute.AttributeDto;
import ru.nonamejack.audioshop.dto.request.ProductAttributeDtoRequest;
import ru.nonamejack.audioshop.exception.custom.NotFoundException;
import ru.nonamejack.audioshop.exception.custom.ResourceCreationException;
import ru.nonamejack.audioshop.mapper.AttributeMapper;
import ru.nonamejack.audioshop.mapper.ProductMapper;
import ru.nonamejack.audioshop.mapper.factory.AttributeMapperFactory;
import ru.nonamejack.audioshop.model.Attribute;
import ru.nonamejack.audioshop.model.Product;
import ru.nonamejack.audioshop.model.ProductAttribute;
import ru.nonamejack.audioshop.model.ProductAttributeKey;
import ru.nonamejack.audioshop.repository.ProductAttributeRepository;

import java.util.List;
import java.util.Optional;


@Service
public class ProductAttributeService {
    private final ProductAttributeRepository productAttributeRepository;
    private final ProductService productService;

    private final ProductMapper productMapper;
    private final AttributeService attributeService;
    private final AttributeMapperFactory attributeMapperFactory;
    private final AttributeMapper attributeMapper;

    private final MessageService messageService;

    public ProductAttributeService(ProductAttributeRepository productAttributeRepository, ProductService productService, ProductMapper productMapper, AttributeService attributeService, AttributeMapperFactory attributeMapperFactory, AttributeMapper attributeMapper, MessageService messageService) {
        this.productAttributeRepository = productAttributeRepository;

        this.productService = productService;
        this.productMapper = productMapper;
        this.attributeService = attributeService;
        this.attributeMapperFactory = attributeMapperFactory;
        this.attributeMapper = attributeMapper;
        this.messageService = messageService;
    }

    public void deleteAttribute(Integer productId, Integer attributeId) {
        ProductAttributeKey id = attributeMapper.createKey(productId, attributeId);
        productAttributeRepository.deleteById(id);
    }



    public void setProductAttribute(Integer productId, Integer attributeId, ProductAttributeDtoRequest productAttributeDtoRequest) {
        ProductAttributeKey key = attributeMapper.createKey(productId, attributeId);
        Optional<ProductAttribute> productAttributeOpt = productAttributeRepository.findById(key);
        ProductAttribute productAttribute;
        if (productAttributeOpt.isPresent()) {
            productAttribute = productAttributeOpt.get();
            attributeMapperFactory.updateProductAttribute(productAttribute, productAttributeDtoRequest);
        }
        else{
            Attribute attribute = attributeService.getAttributeById(attributeId);
            Product product = productService.getProductById(productId);
            productAttribute = attributeMapperFactory.createProductAttribute(product, attribute, productAttributeDtoRequest);
        }
        productAttributeRepository.save(productAttribute);
    }

    @Transactional
    public List<AttributeDto> getProductAttributes(Integer productId) {
        Product product = productService.getProductById(productId);
        return productMapper.mapAllAttributes(product);
    }


    private ProductAttribute findProductAttributeById(ProductAttributeKey key) {
        return productAttributeRepository.findById(key)
                .orElseThrow(() -> new NotFoundException(messageService.getMessage("error.attribute.not_found")));
    }
}
