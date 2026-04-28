package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentDto;
import com.example.paymentservice.jpa.PaymentEntity;

public interface PaymentService {
    PaymentEntity getPaymentByPaymentId(String paymentId);
    PaymentEntity getPaymentByOrderId(String orderId);
    PaymentDto createPayment(PaymentDto paymentDto);
}
