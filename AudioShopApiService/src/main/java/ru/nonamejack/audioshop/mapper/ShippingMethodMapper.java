package ru.nonamejack.audioshop.mapper;

import org.mapstruct.Mapper;
import ru.nonamejack.audioshop.dto.order.ShippingMethodDto;
import ru.nonamejack.audioshop.model.ShippingMethod;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShippingMethodMapper {

    ShippingMethodDto toDto(ShippingMethod shippingMethod);
    List<ShippingMethodDto> toDtoList(List<ShippingMethod> shippingMethods);

}
