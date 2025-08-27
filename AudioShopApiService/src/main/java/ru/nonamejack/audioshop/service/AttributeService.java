package ru.nonamejack.audioshop.service;

import ru.nonamejack.audioshop.dto.attribute.AttributeCategoryDto;
import ru.nonamejack.audioshop.dto.attribute.AttributeDto;
import ru.nonamejack.audioshop.dto.request.AttributeDtoRequest;
import ru.nonamejack.audioshop.exception.custom.NotFoundException;
import ru.nonamejack.audioshop.exception.custom.ResourceCreationException;
import ru.nonamejack.audioshop.mapper.AttributeMapper;
import ru.nonamejack.audioshop.model.Attribute;
import ru.nonamejack.audioshop.model.Category;
import ru.nonamejack.audioshop.model.CategoryAttribute;
import ru.nonamejack.audioshop.model.enums.ValueType;
import ru.nonamejack.audioshop.repository.AttributeRepository;
import ru.nonamejack.audioshop.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nonamejack.audioshop.repository.util.AttributeSpecification;

import java.util.List;

@Service
public class AttributeService {

    private final CategoryRepository categoryRepository;
    private final MessageService messageService;
    private final AttributeMapper attributeMapper;

    private final AttributeRepository attributeRepository;
    public AttributeService(CategoryRepository categoryRepository, MessageService messageService, AttributeMapper attributeMapper, AttributeRepository attributeRepository) {
        this.categoryRepository = categoryRepository;
        this.messageService = messageService;
        this.attributeMapper = attributeMapper;
        this.attributeRepository = attributeRepository;
    }


    public List<AttributeDto> getAllAttributes(ValueType valueType, String name){
        List<Attribute> attributes = attributeRepository.findAll(AttributeSpecification.buildSpecification(name, valueType));
        return attributeMapper.toDtoList(attributes);
    }

    public AttributeDto createAttribute(AttributeDtoRequest dto){
        if (attributeRepository.existsByName(dto.name())){
            throw new ResourceCreationException(messageService.getMessage("error.attribute.already_exists"));
        }
        Attribute attribute = attributeMapper.toEntity(dto);
        Attribute saved = attributeRepository.save(attribute);
        return attributeMapper.toDto(saved);
    }

    public void updateAttribute(Integer attributeId, AttributeDtoRequest dto){
        Attribute attribute = getAttributeById(attributeId);
        if (dto.name() != null && !dto.name().equals(attribute.getName())){
            if (attributeRepository.existsByName(dto.name())){
                throw new ResourceCreationException(messageService.getMessage("error.attribute.already_exists"));
            }
            attributeMapper.updateEntity(dto, attribute);
            attributeRepository.save(attribute);
        }
    }

    public void deleteAttribute(Integer attributeId){
        attributeRepository.deleteById(attributeId);
    }

    @Transactional(readOnly = true)
    public List<AttributeCategoryDto> getAttributeCategory(Integer categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(messageService.getMessage("error.category.not_found", categoryId)));
        List<CategoryAttribute> categoryAttribute = category.getCategoryAttributeList();
        return  attributeMapper.toAttributeCategoryList(categoryAttribute);
    }

    public Attribute getAttributeById(Integer attributeId){
        return attributeRepository.findById(attributeId)
                .orElseThrow(() -> new NotFoundException(messageService.getMessage("error.attribute.not_found", attributeId)));
    }
}

