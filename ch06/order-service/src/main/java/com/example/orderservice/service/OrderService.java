package com.example.orderservice.service;

import com.example.orderservice.model.Order;

public interface OrderService {
    Order createOrder(Order orderDetails);
    Order getOrderByOrderId(String orderId);
    Iterable<Order> getOrdersByUserId(String userId);
}
