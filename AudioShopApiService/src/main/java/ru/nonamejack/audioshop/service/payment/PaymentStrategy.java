package ru.nonamejack.audioshop.service.payment;

import ru.nonamejack.audioshop.dto.order.OrderItemDto;
import ru.nonamejack.audioshop.dto.payment.PaymentDetails;
import ru.nonamejack.audioshop.dto.payment.PaymentResult;
import ru.nonamejack.audioshop.model.Order;
import ru.nonamejack.audioshop.model.enums.PaymentType;
import ru.nonamejack.audioshop.model.ShippingMethod;
import ru.nonamejack.audioshop.model.User;

import java.util.List;

public interface PaymentStrategy {
    boolean isAvailableFor(User user, List<OrderItemDto> items);

    boolean validate(PaymentDetails paymentDetails);

    boolean isCompatibleWith(ShippingMethod shippingMethod);

    /**
     * Обрабатывает платёж для заказа
     * @param order заказ для оплаты
     * @param paymentDetails детали платежа
     * @return результат обработки платежа
     */
    PaymentResult processPayment(Order order, PaymentDetails paymentDetails);

    void confirmPayment(String paymentId);
    PaymentType getType();
}
