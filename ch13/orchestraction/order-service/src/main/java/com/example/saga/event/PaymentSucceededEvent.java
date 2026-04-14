package com.example.saga.event;

import java.time.Instant;

public class PaymentSucceededEvent {
    private String userId;
    private String paymentId;
    private String orderId;
    private Instant currentTime;

    public PaymentSucceededEvent() {
    }

    public PaymentSucceededEvent(String userId, String paymentId, String orderId) {
        this.userId = userId;
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.currentTime = Instant.now();
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getUserId() {
        return userId;
    }

    public Instant currentTime() {
        return null;
    }
}