package ru.nonamejack.audioshop.service.shipping;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ru.nonamejack.audioshop.dto.order.OrderItemDto;
import ru.nonamejack.audioshop.dto.shipping.ShippingDetails;
import ru.nonamejack.audioshop.model.ShippingMethod;
import ru.nonamejack.audioshop.model.enums.ShippingType;
import ru.nonamejack.audioshop.model.User;
import ru.nonamejack.audioshop.repository.ShippingMethodRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShippingService {
    private final ShippingMethodRepository shippingMethodRepository;
    private final List<ShippingStrategy> strategies;
    private final Map<ShippingType, ShippingStrategy> strategyMap = new HashMap<>();

    public ShippingService(ShippingMethodRepository shippingMethodRepository, List<ShippingStrategy> strategies) {
        this.shippingMethodRepository = shippingMethodRepository;
        this.strategies = strategies;
    }
    @PostConstruct
    public void init() {
        for (ShippingStrategy strategy : strategies) {
            strategyMap.put(strategy.getType(), strategy);
        }
    }

    public List<ShippingMethod> getAvailableShippingMethods(User user, List<OrderItemDto> items) {
        return shippingMethodRepository.findAll().stream()
                .filter(method -> {
                    ShippingStrategy strategy = strategyMap.get(method.getShippingType());
                    return strategy != null && method.isActive() && strategy.isAvailableFor(user, items);
                })
                .toList();
    }

    public boolean validate(ShippingType shippingType, ShippingDetails shippingDetails) {
        ShippingStrategy strategy = strategyMap.get(shippingType);
        return strategy.validate(shippingDetails);
    }
}
