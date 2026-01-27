package com.example.orderservice.dto;

import lombok.Data;

@Data
public class PaymentReceipt {
    private String paymentId;
    private String status; // APPROVED / DECLINED
    private String message;

    public PaymentReceipt() {}

    public PaymentReceipt(String paymentId, String status, String message) {
        this.paymentId = paymentId;
        this.status = status;
        this.message = message;
    }
}
