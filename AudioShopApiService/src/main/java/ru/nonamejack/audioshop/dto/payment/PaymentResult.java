package ru.nonamejack.audioshop.dto.payment;
import lombok.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResult {
    private PaymentStatus status;
    private String redirectUrl; // для онлайн платежей
    private String paymentId; // ID платежа в системе
    private String message; // сообщение об ошибке или информация

    public enum PaymentStatus {
        SUCCESS, // платёж прошёл успешно
        PENDING, // требуется перенаправление на оплату
        FAILED   // ошибка оплаты
    }

    public static PaymentResult success(String paymentId) {
        return PaymentResult.builder()
                .status(PaymentStatus.SUCCESS)
                .paymentId(paymentId)
                .build();
    }

    public static PaymentResult pending(String redirectUrl, String paymentId) {
        return PaymentResult.builder()
                .status(PaymentStatus.PENDING)
                .redirectUrl(redirectUrl)
                .paymentId(paymentId)
                .build();
    }

    public static PaymentResult failed(String message) {
        return PaymentResult.builder()
                .status(PaymentStatus.FAILED)
                .message(message)
                .build();
    }
}