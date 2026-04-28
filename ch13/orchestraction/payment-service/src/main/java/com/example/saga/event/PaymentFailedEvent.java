package com.example.saga.event;

import lombok.Getter;

import java.time.Instant;

@Getter
public class PaymentFailedEvent {
    private String userId;
    private String paymentId;
    private String orderId;
    private String reason;
    private Instant currentTime;

    public PaymentFailedEvent() {
    }

    public PaymentFailedEvent(String userId, String orderId, String reason) {
        this.userId = userId;
        this.orderId = orderId;
        this.reason = reason;
        this.currentTime = Instant.now();
    }

    public PaymentFailedEvent(String userId, String paymentId, String orderId, String reason) {
        this.userId = userId;
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.reason = reason;
        this.currentTime = Instant.now();
    }
}