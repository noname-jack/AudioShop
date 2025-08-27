package ru.nonamejack.audioshop.dto.order;

import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    @Positive
    private Integer productId;
    @Min(1)
    private int quantity;

    private BigDecimal price;

    private String productName;

    private String productImage;
}
