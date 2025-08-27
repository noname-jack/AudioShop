package ru.nonamejack.audioshop.service.shipping;

import ru.nonamejack.audioshop.dto.order.OrderItemDto;
import ru.nonamejack.audioshop.dto.shipping.ShippingDetails;
import ru.nonamejack.audioshop.model.Order;
import ru.nonamejack.audioshop.model.enums.ShippingType;
import ru.nonamejack.audioshop.model.User;

import java.util.List;

public interface ShippingStrategy {
    boolean isAvailableFor(User user, List<OrderItemDto> items);

    boolean validate(ShippingDetails shippingDetails);

    void processShipping(Order order);

    ShippingType getType();
}
