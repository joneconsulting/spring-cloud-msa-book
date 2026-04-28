package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentReceipt;
import com.example.paymentservice.dto.PaymentRequest;

public interface PaymentService {
    PaymentReceipt createPayment(PaymentRequest request);
}
