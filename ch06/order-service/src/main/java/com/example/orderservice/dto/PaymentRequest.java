package com.example.orderservice.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String orderId;
    private String userId;
    private String paymentMethod;
    private Integer amount;
}