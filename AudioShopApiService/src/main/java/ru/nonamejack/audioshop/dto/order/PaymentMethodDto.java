package ru.nonamejack.audioshop.dto.order;

import ru.nonamejack.audioshop.model.enums.PaymentType;

public record PaymentMethodDto (
        Long id,
        PaymentType paymentType,
        String description){
}
