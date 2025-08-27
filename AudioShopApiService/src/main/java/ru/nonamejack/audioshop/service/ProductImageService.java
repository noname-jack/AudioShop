package ru.nonamejack.audioshop.service;

import org.springframework.stereotype.Service;
import ru.nonamejack.audioshop.exception.custom.NotFoundException;
import ru.nonamejack.audioshop.model.ProductImage;
import ru.nonamejack.audioshop.repository.ProductImageRepository;

@Service
public class ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ImageService imageService;
    private final MessageService messageService;

    public ProductImageService(ProductImageRepository productImageRepository, ImageService imageService, MessageService messageService) {
        this.productImageRepository = productImageRepository;
        this.imageService = imageService;
        this.messageService = messageService;
    }

    public void deleteImageToProduct(Integer imageId){
        ProductImage productImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException(
                        messageService.getMessage("error.product.image.not_found", imageId)
                ));

        imageService.deleteImage(productImage.getImageUrl());
        productImageRepository.delete(productImage);
    }
}
