package ru.nonamejack.audioshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.nonamejack.audioshop.dto.order.OrderDto;
import ru.nonamejack.audioshop.dto.order.OrderItemDto;
import ru.nonamejack.audioshop.dto.request.OrderItemRequest;
import ru.nonamejack.audioshop.model.Order;
import ru.nonamejack.audioshop.model.Product;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productImage", source = "product.mainImage")
    @Mapping(target = "quantity", source = "item.quantity")
    @Mapping(target = "productId", source = "item.productId")
    OrderItemDto toOrderItemDto(OrderItemRequest item, Product product);


    default List<OrderItemDto> toOrderItemDtoList(List<OrderItemRequest> items, List<Product> products) {
        if (items == null || products == null || items.size() != products.size()) {
            throw new IllegalArgumentException("Размеры списков items и products должны совпадать");
        }

        List<OrderItemDto> result = new java.util.ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            result.add(toOrderItemDto(items.get(i), products.get(i)));
        }
        return result;
    }


    OrderDto toOrderDto(Order order);
}
