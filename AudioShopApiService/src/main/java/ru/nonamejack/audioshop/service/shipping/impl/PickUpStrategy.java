package ru.nonamejack.audioshop.service.shipping.impl;

import org.springframework.stereotype.Component;
import ru.nonamejack.audioshop.dto.order.OrderItemDto;
import ru.nonamejack.audioshop.dto.shipping.ShippingDetails;
import ru.nonamejack.audioshop.model.Order;
import ru.nonamejack.audioshop.model.enums.ShippingType;
import ru.nonamejack.audioshop.model.User;
import ru.nonamejack.audioshop.service.shipping.ShippingStrategy;

import java.util.List;

@Component
public class PickUpStrategy implements ShippingStrategy {
    @Override
    public boolean isAvailableFor(User user, List<OrderItemDto> items) {
        return true; // Всегда доступен
    }

    @Override
    public boolean validate(ShippingDetails shippingDetails) {
        return true;// Доп проверок не требуется
    }

    @Override
    public void processShipping(Order order) {

    }

    @Override
    public ShippingType getType() {
        return ShippingType.PICKUP;
    }
}
