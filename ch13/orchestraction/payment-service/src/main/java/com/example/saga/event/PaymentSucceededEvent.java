package com.example.saga.event;

import lombok.Getter;

import java.time.Instant;

@Getter
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
}