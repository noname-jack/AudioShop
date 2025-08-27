package ru.nonamejack.audioshop.dto.order;

import ru.nonamejack.audioshop.model.enums.ShippingType;

public record ShippingMethodDto(
        Long id,
        ShippingType shippingType,
        String description
) {
}
