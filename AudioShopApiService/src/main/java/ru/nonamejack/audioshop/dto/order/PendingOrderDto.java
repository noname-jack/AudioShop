package ru.nonamejack.audioshop.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nonamejack.audioshop.dto.payment.PaymentDetails;
import ru.nonamejack.audioshop.dto.shipping.ShippingDetails;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingOrderDto {
    private String pendingOrderId;
    private Integer userId;
    private List<OrderItemDto> items;
    private List<ShippingMethodDto> availableShippingMethods;
    private List<PaymentMethodDto> availablePaymentMethods;

    private ShippingMethodDto selectShippingMethod;
    private PaymentMethodDto selectPaymentMethod;

    private ShippingDetails shippingDetails;
    private PaymentDetails paymentDetails;

    private BigDecimal totalPrice;
}
