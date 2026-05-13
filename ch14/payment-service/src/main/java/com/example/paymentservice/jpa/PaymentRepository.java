package com.example.paymentservice.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByPaymentId(String productId);
    Optional<PaymentEntity> findByOrderId(String orderId);
}
