package com.example.orderservice.jpa;

import com.example.orderservice.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Order findByOrderId(String orderId);
    Iterable<Order> findByUserId(String userId);
}
