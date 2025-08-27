package ru.nonamejack.audioshop.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.nonamejack.audioshop.dto.ApiResponse;
import ru.nonamejack.audioshop.dto.order.OrderItemDto;
import ru.nonamejack.audioshop.dto.order.OrderResult;
import ru.nonamejack.audioshop.dto.order.PendingOrderDto;
import ru.nonamejack.audioshop.dto.request.OrderItemRequest;
import ru.nonamejack.audioshop.model.Order;
import ru.nonamejack.audioshop.service.OrderService;
import ru.nonamejack.audioshop.util.SecurityContextUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/order")
@Validated
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create-initial")
    public ApiResponse<PendingOrderDto> createOrder(@RequestBody @Valid List<OrderItemRequest> items) {
        Integer userId = SecurityContextUtil.getCurrentUser();
        return ApiResponse.success(orderService.createInitialOrder(userId,items));
    }
    @GetMapping("/pending/{pendingOrderId}")
    public ApiResponse<PendingOrderDto> getPendingOrder(
            @PathVariable String pendingOrderId) {
        Integer userId = SecurityContextUtil.getCurrentUser();
        return ApiResponse.success(orderService.getPendingOrder(pendingOrderId, userId));
    }


    @DeleteMapping("/pending/{pendingOrderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable String pendingOrderId) {
        Integer userId = SecurityContextUtil.getCurrentUser();
        orderService.cancelPendingOrder(pendingOrderId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout/{pendingOrderId}")
    public ApiResponse<Map<String, Object>>checkout(@PathVariable String pendingOrderId) {
        Integer userId = SecurityContextUtil.getCurrentUser();
        OrderResult result = orderService.checkoutOrder(pendingOrderId, userId);
        return switch (result.getStatus()) {
            case SUCCESS -> ApiResponse.success(Map.of(
                    "status", "success",
                    "message", "Заказ успешно создан",
                    "order", result.getOrder()
            ));

            case PAYMENT_REQUIRED -> ApiResponse.success(Map.of(
                    "status", "payment_required",
                    "message", "Требуется оплата",
                    "order", result.getOrder(),
                    "redirectUrl", result.getRedirectUrl()
            ));

            case FAILED -> ApiResponse.error(result.getMessage(), HttpStatus.BAD_REQUEST);
        };
    }

}
