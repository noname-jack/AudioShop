package ru.nonamejack.audioshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;
import ru.nonamejack.audioshop.dto.BigDecimalRangeDto;
import ru.nonamejack.audioshop.dto.DoubleRangeDto;
import ru.nonamejack.audioshop.dto.attribute.AttributeCategoryDto;
import ru.nonamejack.audioshop.dto.category.CategoryDto;
import ru.nonamejack.audioshop.dto.category.CategoryMiniDto;
import ru.nonamejack.audioshop.dto.category.CategoryTreeDto;
import ru.nonamejack.audioshop.dto.category.FilterOptionDto;
import ru.nonamejack.audioshop.dto.request.CategoryRequest;
import ru.nonamejack.audioshop.exception.custom.EntityNotEmptyException;
import ru.nonamejack.audioshop.exception.custom.NotFoundException;
import ru.nonamejack.audioshop.mapper.BrandMapper;
import ru.nonamejack.audioshop.mapper.CategoryDtoMapper;
import ru.nonamejack.audioshop.mapper.factory.AttributeFilterFactory;
import ru.nonamejack.audioshop.model.Brand;
import ru.nonamejack.audioshop.model.Category;
import ru.nonamejack.audioshop.repository.BrandRepository;
import ru.nonamejack.audioshop.repository.CategoryRepository;
import ru.nonamejack.audioshop.repository.ProductRepository;
import ru.nonamejack.audioshop.repository.util.CategorySpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nonamejack.audioshop.util.PriceRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDtoMapper categoryDtoMapper;
    private final MessageService messageService;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final AttributeService attributeService;
    private final AttributeFilterFactory attributeFilterFactory;
    private final BrandMapper brandMapper;
    private final ImageService imageService;

    public CategoryService(CategoryRepository categoryRepository, CategoryDtoMapper categoryDtoMapper, MessageService messageService, BrandRepository brandRepository, ProductRepository productRepository, AttributeService attributeService, AttributeFilterFactory attributeFilterFactory, BrandMapper brandMapper, ImageService imageService) {
        this.categoryRepository = categoryRepository;
        this.categoryDtoMapper = categoryDtoMapper;
        this.messageService = messageService;
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
        this.attributeService = attributeService;
        this.attributeFilterFactory = attributeFilterFactory;
        this.brandMapper = brandMapper;
        this.imageService = imageService;
    }

    public List<CategoryTreeDto> getCategoryTree() {
        List<Category> allCategories = categoryRepository.findAll();
        Map<Integer, List<Category>> parentIdToChildren = allCategories.stream()
                .collect(Collectors.groupingBy(category -> 
                    category.getParentCategoryId() != null ? category.getParentCategoryId() : 0));
        return buildCategoryTree(parentIdToChildren, 0);
    }


    public CategoryTreeDto getCategoryTreeById(Integer id) {
        Category category = getCategoryEntityById(id);
        return buildCategoryBranch(category);
    }

    private List<CategoryTreeDto> buildCategoryTree(Map<Integer, List<Category>> parentIdToChildren, Integer parentId) {
        List<Category> children = parentIdToChildren.get(parentId);
        if (children == null) {
            return new ArrayList<>();
        }
        return children.stream()
                .map(category -> {
                    CategoryTreeDto dto = categoryDtoMapper.categoryToCategoryTreeDTO(category);
                    dto.setChildren(buildCategoryTree(parentIdToChildren, category.getCategoryId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
    private CategoryTreeDto buildCategoryBranch(Category category) {
        CategoryTreeDto dto = categoryDtoMapper.categoryToCategoryTreeDTO(category);
        List<Category> children = categoryRepository.findByParentCategoryId(category.getCategoryId());

        List<CategoryTreeDto> childrenDtos = children.stream()
                .map(this::buildCategoryBranch)
                .collect(Collectors.toList());

        dto.setChildren(childrenDtos);
        return dto;
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Integer id) {
        Category category = getCategoryEntityById(id);
        return categoryDtoMapper.categoryToCategoryDTO(category);
    }

    public Category getCategoryEntityById(Integer id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageService.getMessage("error.category.not_found", id)));
    }


    public FilterOptionDto getFilterOptionToCategory(Integer categoryId){
        List<Brand> brands = brandRepository.findDistinctBrands(categoryId);
        PriceRange pr = productRepository.findPriceRange(categoryId);
        List<AttributeCategoryDto>  attributeDtoList = attributeService.getAttributeCategory(categoryId);
        attributeDtoList = attributeDtoList.stream()
                .filter(AttributeCategoryDto::isUseFilter)
                .filter(attributeFilterFactory::isSupported)
                .map(dto -> attributeFilterFactory.build(dto, categoryId))
                .toList();
        return new FilterOptionDto(
                categoryId,
                brandMapper.toBrandDtoList(brands),
                new BigDecimalRangeDto(pr.getMinValue(), pr.getMaxValue()),
                attributeDtoList
        );
    }

    public Page<CategoryDto> getAllCategory(String name, Integer parentId, Pageable pageable){
        Specification<Category> spec = CategorySpecification.buildSpecification(name, parentId);
        Page<Category> categoryPage = categoryRepository.findAll(spec, pageable);
        return categoryDtoMapper.toDtoPage(categoryPage);
    }

    public List<CategoryMiniDto> getAllCategoryMini(){
        return categoryRepository.findLeafCategories();
    }

    public void deleteCategory(Integer id){
        if (productRepository.existsByCategoryCategoryId(id)){
            throw new EntityNotEmptyException(messageService.getMessage("error.category.delete_not_empty"));
        }
        categoryRepository.deleteById(id);
    }

    public CategoryDto createCategory(CategoryRequest categoryDto){
        if (categoryDto.parentCategoryId() != null && !categoryRepository.existsById(categoryDto.parentCategoryId())){
            throw new NotFoundException(messageService.getMessage("error.category.not_found", categoryDto.parentCategoryId()));
        }
        Category category = categoryDtoMapper.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryDtoMapper.categoryToCategoryDTO(savedCategory);
    }

    public void addImageToCategory(Integer categoryId, MultipartFile file){
        Category category = getCategoryEntityById(categoryId);
        imageService.saveImageAndAssign(
                file,
                "categories",
                imagePath -> {
                    category.setImgPath(imagePath);
                    return category;
                },
                categoryRepository::save
        );
    }

    public void updateCategory(Integer id, CategoryRequest categoryDto){
        if (categoryDto.parentCategoryId() != null && !categoryRepository.existsById(categoryDto.parentCategoryId())){
            throw new NotFoundException(messageService.getMessage("error.category.not_found", categoryDto.parentCategoryId()));
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageService.getMessage("error.category.not_found", id)));

        categoryDtoMapper.updateCategory(categoryDto, category);
        categoryRepository.save(category);
    }
} 