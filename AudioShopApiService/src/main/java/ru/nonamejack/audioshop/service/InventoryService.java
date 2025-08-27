package ru.nonamejack.audioshop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nonamejack.audioshop.dto.order.OrderItemDto;
import ru.nonamejack.audioshop.dto.order.PendingOrderDto;

@Service
public class InventoryService {

    //TODO сделать проверку на наличие товара
    public boolean isAvailable(Integer productId, Integer quantity) {
        return true;
    }


    public void releaseReservation(PendingOrderDto pendingOrder) {}

    /**
     * Резервирует товары из заказа (уменьшает доступное количество).
     * @param pendingOrder заказ с товарами
     */
    @Transactional
    public void reserveItems(PendingOrderDto pendingOrder) {
        for (OrderItemDto item : pendingOrder.getItems()) {
        }
    }
}
