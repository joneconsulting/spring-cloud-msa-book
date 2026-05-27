package com.example.notificationservice.service;

import com.example.notificationservice.dto.PaymentDto;
import com.example.notificationservice.jpa.PaymentEntity;

public interface PaymentService {
    PaymentEntity getPaymentByPaymentId(String paymentId);
    PaymentEntity getPaymentByOrderId(String orderId);
    PaymentDto createPayment(PaymentDto paymentDto);
}
