package ru.nonamejack.audioshop.service.payment;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ru.nonamejack.audioshop.dto.order.OrderItemDto;
import ru.nonamejack.audioshop.dto.payment.PaymentDetails;
import ru.nonamejack.audioshop.dto.payment.PaymentResult;
import ru.nonamejack.audioshop.model.*;
import ru.nonamejack.audioshop.model.enums.PaymentType;
import ru.nonamejack.audioshop.repository.PaymentMethodRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    private final PaymentMethodRepository paymentMethodRepository;
    private final List<PaymentStrategy> strategies;
    private Map<PaymentType, PaymentStrategy> strategyMap = new HashMap<>();

    public PaymentService(PaymentMethodRepository paymentMethodRepository, List<PaymentStrategy> strategies) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.strategies = strategies;
    }
    @PostConstruct
    public void init() {
        for (PaymentStrategy strategy : strategies) {
            strategyMap.put(strategy.getType(), strategy);
        }
    }

    //TODO сделать выборку доступных методов оплаты
    public List<PaymentMethod> getAvailablePaymentMethods(User user, List<OrderItemDto> items) {
        return paymentMethodRepository.findAll().stream()
                .filter(method -> {
                    PaymentStrategy strategy = strategyMap.get(method.getPaymentType());
                    return strategy != null && strategy.isAvailableFor(user, items);
                })
                .toList();
    }

    public boolean validate(PaymentType paymentType, PaymentDetails details) {
        PaymentStrategy strategy = strategyMap.get(paymentType);
        return strategy != null && strategy.validate(details);
    }

    /**
     * Обрабатывает платёж для заказа
     */
    public PaymentResult processPayment(Order order, PaymentDetails paymentDetails) {
        PaymentType paymentType = order.getPaymentMethod().getPaymentType();
        PaymentStrategy strategy = strategyMap.get(paymentType);

        if (strategy == null) {
            return PaymentResult.failed("Неподдерживаемый метод оплаты: " + paymentType);
        }

        try {
            return strategy.processPayment(order, paymentDetails);
        } catch (Exception e) {
            return PaymentResult.failed("Ошибка обработки платежа: " + e.getMessage());
        }
    }

    /**
     * Подтверждает платёж (для callback'ов)
     */
    public void confirmPayment(String paymentId) {

    }
}
