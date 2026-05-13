package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentDto;
import com.example.paymentservice.jpa.PaymentEntity;

import java.util.Optional;

public interface PaymentService {
    Optional<PaymentEntity> getPaymentByPaymentId(String paymentId);
    Optional<PaymentEntity> getPaymentByOrderId(String orderId);
    PaymentDto createPayment(PaymentDto paymentDto);
}
