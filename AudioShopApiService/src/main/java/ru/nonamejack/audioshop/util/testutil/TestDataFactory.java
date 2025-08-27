package ru.nonamejack.audioshop.util.testutil;

import ru.nonamejack.audioshop.dto.BigDecimalRangeDto;
import ru.nonamejack.audioshop.dto.DoubleRangeDto;
import ru.nonamejack.audioshop.dto.attribute.AttributeDto;
import ru.nonamejack.audioshop.dto.product.AdminProductSummaryDto;
import ru.nonamejack.audioshop.dto.product.ProductDetailsDto;
import ru.nonamejack.audioshop.dto.product.ProductSummaryDto;
import ru.nonamejack.audioshop.dto.product.ReviewDto;
import ru.nonamejack.audioshop.dto.request.AttributeValueFilter;
import ru.nonamejack.audioshop.dto.request.ProductDtoRequest;
import ru.nonamejack.audioshop.dto.request.ProductFilterAdminRequest;
import ru.nonamejack.audioshop.dto.request.ProductFilterRequest;
import ru.nonamejack.audioshop.model.Category;
import ru.nonamejack.audioshop.model.Product;
import ru.nonamejack.audioshop.model.enums.FilterType;
import ru.nonamejack.audioshop.model.enums.ValueType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TestDataFactory {

    public static Product createProduct() {
        Category category = new Category();
        category.setCategoryId(1);

        Product product = new Product();
        product.setCategory(category);
        product.setName("Headphones");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setStock(10);
        product.setActive(true);
        return product;
    }

    public static ProductSummaryDto createProductSummaryDto() {
        return new ProductSummaryDto(
                1,
                "Sony",
                "Headphones",
                BigDecimal.valueOf(99.99),
                5,
                "image.jpg"
        );
    }

    public static ProductDetailsDto createProductDetailsDto() {
        return new ProductDetailsDto(
                1,               // productId
                "Sony",          // brandName
                10,              // brandId
                "Headphones",    // categoryName
                "cat-01",        // categoryId
                "WH-1000XM4",    // name
                "Noise cancelling", // description
                BigDecimal.valueOf(299), // price
                50,                      // stock
                "main.jpg",              // mainImage
                null,                     // imagesMap
                List.of(new AttributeDto(1, "Color", ValueType.STRING, FilterType.ENUM)), // attributes
                List.of(new ReviewDto(1, 4.0, "dd", "user", "", "", new Date())),        // reviews
                true
        );
    }

    public static AdminProductSummaryDto createAdminProductSummaryDto() {
        return new AdminProductSummaryDto(
                1,
                "Sony",
                "Headphones",
                BigDecimal.valueOf(99.99),
                5,
                "image.jpg",
                true
        );
    }

    public static ProductDtoRequest createProductDtoRequest() {
        return new ProductDtoRequest(
                1,                    // categoryId
                2,                    // brandId
                "Headphones",         // name
                "High quality sound", // description
                BigDecimal.valueOf(99.99), // price
                10,                   // stock
                true                  // active
        );
    }

    public static ProductFilterRequest createProductFilterRequest() {
        return new ProductFilterRequest(
                1, // categoryId
                List.of(2, 3), // brandIds
                new BigDecimalRangeDto(BigDecimal.valueOf(10), BigDecimal.valueOf(100)),
                List.of(
                        new AttributeValueFilter(
                                1,
                                FilterType.ENUM,
                                ValueType.STRING,
                                List.of("red", "black"),
                                null, null, null
                        ),
                        new AttributeValueFilter(
                                2,
                                FilterType.RANGE,
                                ValueType.DOUBLE,
                                null, null, null,
                                new DoubleRangeDto(10.0, 100.0)
                        ),
                        new AttributeValueFilter(
                                3,
                                FilterType.BOOLEAN,
                                ValueType.BOOLEAN,
                                null, null,
                                List.of(true, false),
                                null
                        )
                )
        );
    }

    public static ProductFilterAdminRequest createProductFilterAdminRequest() {
        return new ProductFilterAdminRequest(
                1,          // categoryId
                List.of(2,3),
                true,       // activeOnly
                "Nova2"     // search keyword
        );
    }

    public static ProductFilterRequest createProductFilterRequestWithPriceRange(BigDecimalRangeDto range) {
        return new ProductFilterRequest(
                1,
                null,
                range,
                null
        );
    }
}
