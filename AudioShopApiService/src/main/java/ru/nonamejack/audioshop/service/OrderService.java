package ru.nonamejack.audioshop.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nonamejack.audioshop.dto.order.*;
import ru.nonamejack.audioshop.dto.payment.PaymentDetails;
import ru.nonamejack.audioshop.dto.payment.PaymentResult;
import ru.nonamejack.audioshop.dto.request.OrderItemRequest;
import ru.nonamejack.audioshop.dto.shipping.ShippingDetails;
import ru.nonamejack.audioshop.exception.custom.InsufficientStockException;
import ru.nonamejack.audioshop.exception.custom.NotFoundException;
import ru.nonamejack.audioshop.mapper.OrderMapper;
import ru.nonamejack.audioshop.mapper.PaymentMethodMapper;
import ru.nonamejack.audioshop.mapper.ShippingMethodMapper;
import ru.nonamejack.audioshop.model.*;
import ru.nonamejack.audioshop.model.enums.OrderStatus;
import ru.nonamejack.audioshop.model.enums.PaymentType;
import ru.nonamejack.audioshop.model.enums.ShippingType;
import ru.nonamejack.audioshop.repository.OrderRepository;
import ru.nonamejack.audioshop.service.payment.PaymentService;
import ru.nonamejack.audioshop.service.shipping.ShippingService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для управления заказами:
 * - создание предварительных заказов
 * - проверка доступности товаров
 * - выбор способов оплаты и доставки
 * - оформление и обработка заказа
 * - взаимодействие с Redis для хранения временных заказов
 */
@Service
public class OrderService {

    // Зависимости сервисов и репозиториев
    private final InventoryService inventoryService;       // управление остатками
    private final PaymentService paymentService;           // обработка оплаты
    private final ShippingService shippingService;         // обработка доставки
    private final UserService userService;                 // управление пользователями
    private final ProductService productService;           // работа с товарами
    private final OrderRepository orderRepository;         // работа с заказами в БД

    // Мапперы для преобразования сущностей в DTO
    private final PaymentMethodMapper paymentMethodMapper;
    private final ShippingMethodMapper shippingMethodMapper;
    private final OrderMapper orderMapper;

    // Redis для хранения временных заказов
    private final RedisTemplate<String, Object> redisTemplate;

    // Константы для ключей и времени жизни заказов в Redis
    private static final String PENDING_ORDER_KEY_PREFIX = "pending_order:";
    private static final String USER_PENDING_ORDERS_KEY_PREFIX = "user_pending_orders:";
    private static final Duration PENDING_ORDER_TTL = Duration.ofHours(1);

    // Конструктор с внедрением зависимостей
    public OrderService(
            InventoryService inventoryService,
            PaymentService paymentService,
            ShippingService shippingService,
            UserService userService,
            ProductService productService,
            OrderRepository orderRepository,
            PaymentMethodMapper paymentMethodMapper,
            ShippingMethodMapper shippingMethodMapper,
            OrderMapper orderMapper,
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.shippingService = shippingService;
        this.userService = userService;
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.paymentMethodMapper = paymentMethodMapper;
        this.shippingMethodMapper = shippingMethodMapper;
        this.orderMapper = orderMapper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Создание предварительного заказа
     * - проверяем доступность товаров
     * - подбираем методы доставки и оплаты
     * - сохраняем заказ во временное хранилище Redis
     */
    public PendingOrderDto createInitialOrder(Integer userId, List<OrderItemRequest> items) {
        // Получаем пользователя
        User user = userService.getCurrentUserById(userId);

        // Проверяем доступность товаров
        List<Product> productList = validateItemsAvailability(items);

        // Преобразуем товары в DTO
        List<OrderItemDto> orderItems = orderMapper.toOrderItemDtoList(items, productList);

        // Получаем доступные методы доставки и оплаты
        List<ShippingMethod> availableShippingMethods = shippingService.getAvailableShippingMethods(user, orderItems);
        List<PaymentMethod> availablePaymentMethods = paymentService.getAvailablePaymentMethods(user, orderItems);

        // Создаём объект предварительного заказа
        PendingOrderDto pendingOrder = PendingOrderDto.builder()
                .pendingOrderId(UUID.randomUUID().toString())
                .userId(userId)
                .items(orderItems)
                .availableShippingMethods(shippingMethodMapper.toDtoList(availableShippingMethods))
                .availablePaymentMethods(paymentMethodMapper.toDtoList(availablePaymentMethods))
                .totalPrice(calculateTotalPrice(orderItems))
                .build();

        // Сохраняем заказ в Redis с TTL
        String orderKey = PENDING_ORDER_KEY_PREFIX + pendingOrder.getPendingOrderId();
        redisTemplate.opsForValue().set(orderKey, pendingOrder, PENDING_ORDER_TTL);

        // Добавляем заказ в список заказов пользователя
        String userOrdersKey = USER_PENDING_ORDERS_KEY_PREFIX + userId;
        redisTemplate.opsForSet().add(userOrdersKey, pendingOrder.getPendingOrderId());
        redisTemplate.expire(userOrdersKey, PENDING_ORDER_TTL);

        return pendingOrder;
    }

    /**
     * Получение предварительного заказа из Redis
     */
    public PendingOrderDto getPendingOrder(String pendingOrderId, Integer userId) {
        String key = PENDING_ORDER_KEY_PREFIX + pendingOrderId;

        // Достаём заказ из Redis
        Object obj = Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));

        // Проверяем тип
        if (!(obj instanceof PendingOrderDto pendingOrder)) {
            throw new IllegalStateException("Неверный тип данных в Redis для заказа: " + pendingOrderId);
        }

        // Проверяем, что заказ принадлежит пользователю
        if (!pendingOrder.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Нет доступа к заказу");
        }
        return pendingOrder;
    }

    /**
     * Отмена предварительного заказа
     * - освобождаем резерв товаров
     * - удаляем заказ из Redis
     */
    public void cancelPendingOrder(String pendingOrderId, Integer userId) {
        PendingOrderDto pendingOrder = getPendingOrder(pendingOrderId, userId);
        inventoryService.releaseReservation(pendingOrder); // освобождение резерва
        deletePendingOrder(pendingOrderId, pendingOrder.getUserId());
    }

    /**
     * Завершение оформления заказа
     * - проверка данных
     * - создание заказа в БД
     * - резервирование товаров
     * - обработка платежа
     * - обновление статуса заказа
     */
    @Transactional
    public OrderResult checkoutOrder(String pendingOrderId, Integer userId) {
        PendingOrderDto pendingOrder = getPendingOrder(pendingOrderId, userId);

        // Проверяем, выбраны ли методы доставки и оплаты
        if (pendingOrder.getSelectShippingMethod() == null || pendingOrder.getSelectPaymentMethod() == null) {
            return OrderResult.failed("Не выбраны методы доставки/оплаты");
        }

        // Валидируем доступность товаров
        try {
            validateDtoItemsAvailability(pendingOrder.getItems());
        } catch (InsufficientStockException e) {
            return OrderResult.failed("Недостаточно товара на складе: " + e.getMessage());
        }

        // Получаем данные доставки и оплаты
        ShippingType shippingType = pendingOrder.getSelectShippingMethod().shippingType();
        ShippingDetails shippingDetails = pendingOrder.getShippingDetails();
        PaymentType paymentType = pendingOrder.getSelectPaymentMethod().paymentType();
        PaymentDetails paymentDetails = pendingOrder.getPaymentDetails();

        // Валидируем данные
        if (!shippingService.validate(shippingType, shippingDetails) ||
                !paymentService.validate(paymentType, paymentDetails)) {
            return OrderResult.failed("Неверно указаны данные для доставки/оплаты");
        }

        try {
            // Создаём и сохраняем заказ в БД
            Order order = createOrder(pendingOrder);
            Order savedOrder = orderRepository.save(order);

            // Резервируем товары
            inventoryService.reserveItems(pendingOrder);

            // Проводим оплату
            PaymentResult paymentResult = paymentService.processPayment(savedOrder, paymentDetails);

            // Обновляем статус заказа по результату
            updateOrderStatusBasedOnPayment(savedOrder, paymentResult);
            savedOrder = orderRepository.save(savedOrder);

            // Удаляем заказ из Redis
            deletePendingOrder(pendingOrderId, userId);

            // Преобразуем заказ в DTO
            OrderDto orderDto = orderMapper.toOrderDto(savedOrder);

            // Возвращаем результат
            return switch (paymentResult.getStatus()) {
                case SUCCESS -> OrderResult.success(orderDto);
                case PENDING -> OrderResult.paymentRequired(orderDto, paymentResult.getRedirectUrl());
                case FAILED -> {
                    rollbackOrder(savedOrder); // откат при неудаче
                    yield OrderResult.failed("Ошибка оплаты: " + paymentResult.getMessage());
                }
            };

        } catch (Exception e) {
            return OrderResult.failed("Ошибка создания заказа: " + e.getMessage());
        }
    }

    /**
     * Проверка доступности списка товаров
     */
    private List<Product> validateItemsAvailability(List<OrderItemRequest> items) {
        for (OrderItemRequest item : items) {
            validateItemAvailability(item);
        }
        return items.stream().map(item -> productService.getProductById(item.productId())).toList();
    }

    private void validateItemAvailability(OrderItemRequest item) {
        if (!inventoryService.isAvailable(item.productId(), item.quantity())) {
            throw new InsufficientStockException("Insufficient stock for product: " + item.productId());
        }
    }

    private void validateDtoItemsAvailability(List<OrderItemDto> items) {
        for (OrderItemDto item : items) {
            validateItemAvailability(item);
        }
    }

    private void validateItemAvailability(OrderItemDto item) {
        if (!inventoryService.isAvailable(item.getProductId(), item.getQuantity())) {
            throw new InsufficientStockException("Insufficient stock for product: " + item.getProductId());
        }
    }

    /**
     * Подсчёт общей стоимости заказа
     */
    private BigDecimal calculateTotalPrice(List<OrderItemDto> items) {
        return items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Удаление предварительного заказа из Redis
     */
    private void deletePendingOrder(String pendingOrderId, Integer userId) {
        String orderKey = PENDING_ORDER_KEY_PREFIX + pendingOrderId;
        String userOrdersKey = USER_PENDING_ORDERS_KEY_PREFIX + userId;
        redisTemplate.delete(orderKey);
        redisTemplate.opsForSet().remove(userOrdersKey, pendingOrderId);
    }

    /**
     * Создание объекта заказа из предварительного заказа
     */
    private Order createOrder(PendingOrderDto pendingOrder) {
        Order order = new Order();
        order.setUser(userService.getUserById(pendingOrder.getUserId()));
        order.setTotalAmount(pendingOrder.getTotalPrice());
        order.setStatus(OrderStatus.CREATED); // начальный статус
        order.setPlacedAt(OffsetDateTime.now());
        order.setUpdatedAt(OffsetDateTime.now());
        // TODO: Установить методы доставки и оплаты
        return order;
    }

    /**
     * Откат заказа (при ошибке оплаты)
     */
    private void rollbackOrder(Order order) {
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(OffsetDateTime.now());
        orderRepository.save(order);
    }

    /**
     * Обновление статуса заказа в зависимости от результата оплаты
     */
    private Order updateOrderStatusBasedOnPayment(Order order, PaymentResult paymentResult) {
        switch (paymentResult.getStatus()) {
            case SUCCESS -> order.setStatus(OrderStatus.PAID);
            case PENDING -> order.setStatus(OrderStatus.PENDING_PAYMENT);
            case FAILED -> order.setStatus(OrderStatus.CANCELLED);
        }
        order.setUpdatedAt(OffsetDateTime.now());
        return order;
    }
}
