package ru.nonamejack.audioshop.controller;

import ru.nonamejack.audioshop.dto.ApiResponse;
import ru.nonamejack.audioshop.dto.product.ReviewDto;
import ru.nonamejack.audioshop.dto.request.CreateReviewRequest;
import ru.nonamejack.audioshop.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Reviews", description = "Управление отзывами о товарах")
@RestController
@RequestMapping("/catalog/products/{productId}/reviews")
@Validated
public class ReviewController {

    private final ReviewService reviewService;
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Добавить отзыв", description = "Создаёт новый отзыв для указанного товара")
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> createReview(
            @PathVariable Integer productId,
            @Valid @RequestBody CreateReviewRequest createReviewRequest) {
        ReviewDto dto = reviewService.createReview(productId, createReviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(dto, HttpStatus.CREATED));
    }

    @Operation(summary = "Список отзывов", description = "Возвращает все отзывы для данного товара")
    @GetMapping
    public ApiResponse<List<ReviewDto>> getReviewsByProduct(@PathVariable Integer productId) {
        return ApiResponse.success(reviewService.getReviewsByProduct(productId));
    }
}
