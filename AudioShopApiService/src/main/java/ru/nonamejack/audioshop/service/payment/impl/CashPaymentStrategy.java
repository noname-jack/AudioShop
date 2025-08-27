package ru.nonamejack.audioshop.service.payment.impl;

import org.springframework.stereotype.Component;
import ru.nonamejack.audioshop.dto.order.OrderItemDto;
import ru.nonamejack.audioshop.dto.payment.PaymentDetails;
import ru.nonamejack.audioshop.dto.payment.PaymentResult;
import ru.nonamejack.audioshop.model.*;
import ru.nonamejack.audioshop.model.enums.PaymentType;
import ru.nonamejack.audioshop.model.enums.ShippingType;
import ru.nonamejack.audioshop.service.payment.PaymentStrategy;

import java.util.List;
import java.util.UUID;


@Component
public class CashPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean isAvailableFor(User user, List<OrderItemDto> items) {
        return true;
    }

    @Override
    public boolean validate(PaymentDetails paymentDetails) {
        return true;
    }

    @Override
    public boolean isCompatibleWith(ShippingMethod shippingMethod) {
        return shippingMethod.getShippingType() == ShippingType.PICKUP;
    }

    @Override
    public PaymentResult processPayment(Order order, PaymentDetails paymentDetails) {
        // Наличная оплата не требует предварительной обработки
        // Платёж будет подтверждён при получении товара
        String paymentId = "cash_" + UUID.randomUUID().toString();
        return PaymentResult.success(paymentId);
    }

    @Override
    public void confirmPayment(String paymentId) {

    }

    @Override
    public PaymentType getType() {
        return PaymentType.CASH;
    }
}
