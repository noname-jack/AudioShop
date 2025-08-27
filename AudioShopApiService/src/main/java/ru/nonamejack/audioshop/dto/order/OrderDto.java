package ru.nonamejack.audioshop.dto.order;

import lombok.Data;
import ru.nonamejack.audioshop.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long orderId;
    private Long userId;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private OffsetDateTime placedAt;
    private OffsetDateTime updatedAt;

    private PaymentMethodDto paymentMethod;
    private ShippingMethodDto shippingMethod;

    private List<OrderItemDto> items;
}
