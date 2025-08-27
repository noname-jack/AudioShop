package ru.nonamejack.audioshop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nonamejack.audioshop.dto.attribute.AttributeCategoryDto;
import ru.nonamejack.audioshop.dto.request.CategoryAttributeDtoRequest;
import ru.nonamejack.audioshop.exception.custom.NotFoundException;
import ru.nonamejack.audioshop.exception.custom.ResourceCreationException;
import ru.nonamejack.audioshop.mapper.AttributeMapper;
import ru.nonamejack.audioshop.model.Attribute;
import ru.nonamejack.audioshop.model.Category;
import ru.nonamejack.audioshop.model.CategoryAttribute;
import ru.nonamejack.audioshop.model.CategoryAttributeKey;
import ru.nonamejack.audioshop.repository.CategoryAttributeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryAttributeService {

    private final CategoryService categoryService;
    private  final AttributeMapper attributeMapper;
    private final AttributeService attributeService;
    private final CategoryAttributeRepository categoryAttributeRepository;

    public CategoryAttributeService(CategoryService categoryService, AttributeMapper attributeMapper, AttributeService attributeService, CategoryAttributeRepository categoryAttributeRepository) {
        this.categoryService = categoryService;
        this.attributeMapper = attributeMapper;
        this.attributeService = attributeService;
        this.categoryAttributeRepository = categoryAttributeRepository;
    }

    @Transactional
    public List<AttributeCategoryDto> getCategoryAttribute(Integer categoryId){
        Category category= categoryService.getCategoryEntityById(categoryId);
        return attributeMapper.toAttributeCategoryList(category.getCategoryAttributeList());
    }

    @Transactional
    public void setCategoryAttribute(Integer categoryId, Integer attributeId, CategoryAttributeDtoRequest request){
        CategoryAttributeKey key = buildCategoryAttributeKey(categoryId, attributeId);
        Optional<CategoryAttribute> optionalCategoryAttribute = categoryAttributeRepository.findById(key);
        CategoryAttribute categoryAttribute;
        if (optionalCategoryAttribute.isPresent()){
            categoryAttribute = optionalCategoryAttribute.get();
            updateCategoryAttributeFields(categoryAttribute, request);
        }
        else {
            categoryAttribute = buildCategoryAttribute(key, request);
        }
        categoryAttributeRepository.save(categoryAttribute);
    }

    @Transactional
    public void deleteCategoryAttribute(Integer categoryId, Integer attributeId) {
        CategoryAttributeKey key = buildCategoryAttributeKey(categoryId, attributeId);
        categoryAttributeRepository.deleteById(key);
    }

    private CategoryAttributeKey buildCategoryAttributeKey(Integer categoryId, Integer attributeId) {
        Category category = categoryService.getCategoryEntityById(categoryId);
        Attribute attribute = attributeService.getAttributeById(attributeId);

        return new CategoryAttributeKey(category.getCategoryId(), attribute.getAttributeId());
    }



    private CategoryAttribute buildCategoryAttribute(CategoryAttributeKey key, CategoryAttributeDtoRequest request) {
        Category category = categoryService.getCategoryEntityById(key.getCategoryId());
        Attribute attribute = attributeService.getAttributeById(key.getAttributeId());

        CategoryAttribute categoryAttribute = new CategoryAttribute();
        categoryAttribute.setCategoryAttributeKey(key);
        categoryAttribute.setCategory(category);
        categoryAttribute.setAttribute(attribute);
        updateCategoryAttributeFields(categoryAttribute, request);

        return categoryAttribute;
    }

    private void updateCategoryAttributeFields(CategoryAttribute categoryAttribute, CategoryAttributeDtoRequest request) {
        categoryAttribute.setRequired(request.isRequired());
        categoryAttribute.setUseFilter(request.isUseFilter());
    }

}
