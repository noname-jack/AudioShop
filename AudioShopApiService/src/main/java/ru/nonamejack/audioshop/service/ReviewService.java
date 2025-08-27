package ru.nonamejack.audioshop.service;

import ru.nonamejack.audioshop.dto.product.ReviewDto;
import ru.nonamejack.audioshop.dto.request.CreateReviewRequest;
import ru.nonamejack.audioshop.exception.custom.NotFoundException;
import ru.nonamejack.audioshop.mapper.ProductMapper;
import ru.nonamejack.audioshop.mapper.ReviewMapper;
import ru.nonamejack.audioshop.model.Product;
import ru.nonamejack.audioshop.model.ReviewProduct;
import ru.nonamejack.audioshop.repository.ProductRepository;
import ru.nonamejack.audioshop.repository.ReviewProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final MessageService messageService;
    private final ReviewProductRepository reviewProductRepository;

    private final ProductService productService;
    private final ReviewMapper reviewMapper;

    public ReviewService(MessageService messageService, ReviewProductRepository reviewProductRepository, ProductService productService, ReviewMapper reviewMapper) {
        this.messageService = messageService;
        this.reviewProductRepository = reviewProductRepository;
        this.productService = productService;
        this.reviewMapper = reviewMapper;
    }

    @Transactional
    public ReviewDto createReview(Integer productId, CreateReviewRequest createReviewRequest){
        Product product = productService.getProductById(productId);
        ReviewProduct newReviewProduct = reviewMapper.toModel(createReviewRequest);
        newReviewProduct.setProduct(product);
        reviewProductRepository.save(newReviewProduct);
        return reviewMapper.toReviewDto(newReviewProduct);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByProduct(Integer productId) {
        Product product = productService.getProductById(productId);
        // Загружаем все отзывы по этому товару
        List<ReviewProduct> reviewProductList = product.getProductReviewList();
        // Конвертируем в DTO
        return reviewProductList.stream()
                .map(reviewMapper::toReviewDto)
                .toList();
    }
}
