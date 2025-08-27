package ru.nonamejack.audioshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.nonamejack.audioshop.dto.order.PaymentMethodDto;
import ru.nonamejack.audioshop.model.PaymentMethod;
import ru.nonamejack.audioshop.repository.PaymentMethodRepository;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    @Mapping(target = "id", source = "id")
    PaymentMethodDto toDto(PaymentMethod paymentMethod);
    List<PaymentMethodDto> toDtoList(List<PaymentMethod> paymentMethods);
}
