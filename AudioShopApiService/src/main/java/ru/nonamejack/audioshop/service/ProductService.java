package ru.nonamejack.audioshop.service;


import org.springframework.web.multipart.MultipartFile;
import ru.nonamejack.audioshop.dto.BigDecimalRangeDto;
import ru.nonamejack.audioshop.dto.DoubleRangeDto;
import ru.nonamejack.audioshop.dto.product.AdminProductSummaryDto;
import ru.nonamejack.audioshop.dto.request.AttributeValueFilter;
import ru.nonamejack.audioshop.dto.product.ProductDetailsDto;
import ru.nonamejack.audioshop.dto.request.ProductDtoRequest;
import ru.nonamejack.audioshop.dto.request.ProductFilterAdminRequest;
import ru.nonamejack.audioshop.dto.request.ProductFilterRequest;
import ru.nonamejack.audioshop.dto.product.ProductSummaryDto;
import ru.nonamejack.audioshop.exception.custom.BadRequestException;
import ru.nonamejack.audioshop.exception.custom.NotFoundException;
import ru.nonamejack.audioshop.mapper.ProductMapper;
import ru.nonamejack.audioshop.repository.util.AttributeFilterSpecificationFactory;
import ru.nonamejack.audioshop.model.*;
import ru.nonamejack.audioshop.repository.ProductRepository;
import ru.nonamejack.audioshop.repository.util.ProductSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;


@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final MessageService messageService;
    private final ProductMapper productMapper;
    private final AttributeFilterSpecificationFactory filterFactory;

    private final ImageService imageService;

    public ProductService(ProductRepository productRepository, MessageService messageService, ProductMapper productMapper, AttributeFilterSpecificationFactory filterFactory, ImageService imageService) {
        this.productRepository = productRepository;
        this.messageService = messageService;
        this.productMapper = productMapper;
        this.filterFactory = filterFactory;
        this.imageService = imageService;
    }


    @Transactional(readOnly= true)
    public ProductSummaryDto getProductSummary(Integer productId) {
        Product product = getProductById(productId);
        return productMapper.toSummary(product);
    }
    @Transactional(readOnly= true)
    public ProductDetailsDto getProductDetails(Integer productId) {
        Product product = getProductById(productId);
        return productMapper.toDetails(product);
    }

    @Transactional(readOnly= true)
    public Page<ProductSummaryDto> findProducts(ProductFilterRequest filterRequest, Pageable pageableRequest) {
        // 1) Базовая спецификация — по выбранной категории
        Specification<Product> combinedSpecification =
                ProductSpecifications.byCategory(filterRequest.categoryId());

        combinedSpecification = combinedSpecification.and(ProductSpecifications.isActive(true));

        combinedSpecification = combinedSpecification.and(
                ProductSpecifications.byBrands(filterRequest.brandIds())
        );

        BigDecimalRangeDto priceRange = filterRequest.priceRange();
        if (priceRange != null) {
            BigDecimal minPrice = priceRange.min();
            BigDecimal maxPrice = priceRange.max();

            // Валидация
            if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
                throw new BadRequestException(messageService.getMessage("error.product.price_min_max"));
            }
            if (minPrice != null) {
                combinedSpecification = combinedSpecification.and(ProductSpecifications.byMinPrice(minPrice));
            }
            if (maxPrice != null) {
                combinedSpecification = combinedSpecification.and(ProductSpecifications.byMaxPrice(maxPrice));
            }
        }
        for (AttributeValueFilter f : filterRequest.attributeFilters()) {
            combinedSpecification = combinedSpecification.and(filterFactory.createSpecification(f));
        }

        Page<Product> productPage = productRepository.findAll(combinedSpecification, pageableRequest);
        return productPage.map(productMapper::toSummary);
    }

    public Page<AdminProductSummaryDto> getAllProductsWithAdmin(ProductFilterAdminRequest productFilterAdminRequest, Pageable pageable) {
        Specification<Product> specification = Specification.where(ProductSpecifications.byCategory(productFilterAdminRequest.categoryId()))
                .and(ProductSpecifications.isActive(productFilterAdminRequest.active()))
                .and(ProductSpecifications.byBrands(productFilterAdminRequest.brandIds()))
                .and(ProductSpecifications.nameContains(productFilterAdminRequest.name()));
        Page<Product> productPage = productRepository.findAll(specification, pageable);
        return productPage.map(productMapper::toAdminProductSummaryDto);
    }


    public ProductDetailsDto createProduct(ProductDtoRequest productDtoRequest){
        Product product = productMapper.toProduct(productDtoRequest);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDetails(savedProduct);
    }

    public void updateProductById(Integer productId,ProductDtoRequest productDtoRequest){
        Product product = getProductById(productId);
        productMapper.updateProductFromDto(productDtoRequest,product);
        productRepository.save(product);
    }

    public void deleteProductById(Integer productId){
        productRepository.deleteById(productId);
    }

    public void addImageToProduct(Integer productId, MultipartFile file){
        Product product = getProductById(productId);

        imageService.saveImageAndAssign(
                file,
                "products",
                imagePath -> {
                    product.setMainImage(imagePath);
                    return product;
                },
                productRepository::save
        );
    }

    public void activeProductById(Integer productId, Boolean active){
        Product product = getProductById(productId);
        product.setActive(active);
        productRepository.save(product);
    }

    public Product getProductById(Integer productId){
        if(productId == null){
            throw new BadRequestException(messageService.getMessage("error.product.not_found"));
        }
        else {
            return productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException(messageService.getMessage("error.product.not_found", productId)));
        }
    }

}
