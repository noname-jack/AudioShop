package ru.nonamejack.audioshop.dto.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResult {
    private OrderResultStatus status;
    private OrderDto order; // созданный заказ (может быть null для PAYMENT_REQUIRED)
    private String redirectUrl; // URL для перенаправления на оплату
    private String message; // дополнительная информация

    public enum OrderResultStatus {
        SUCCESS,         // заказ успешно создан и оплачен
        PAYMENT_REQUIRED, // требуется оплата, заказ создан со статусом PENDING_PAYMENT
        FAILED           // ошибка создания заказа
    }
    public static OrderResult success(OrderDto order) {
        return OrderResult.builder()
                .status(OrderResultStatus.SUCCESS)
                .order(order)
                .build();
    }

    public static OrderResult paymentRequired(OrderDto order, String redirectUrl) {
        return OrderResult.builder()
                .status(OrderResultStatus.PAYMENT_REQUIRED)
                .order(order)
                .redirectUrl(redirectUrl)
                .build();
    }

    public static OrderResult failed(String message) {
        return OrderResult.builder()
                .status(OrderResultStatus.FAILED)
                .message(message)
                .build();
    }
}
