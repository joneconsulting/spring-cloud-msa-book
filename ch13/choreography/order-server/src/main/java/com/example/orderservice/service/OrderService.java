package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.saga.event.ProcessPaymentEvent;

public interface OrderService {
    /* [ch13] */
    ProcessPaymentEvent createOrder(OrderDto orderDetails);

    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrdersByUserId(String userId);
}
