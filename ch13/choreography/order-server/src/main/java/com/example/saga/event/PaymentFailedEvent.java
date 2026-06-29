package com.example.saga.event;

import java.time.Instant;

public class PaymentFailedEvent {
    private String userId;
    private String paymentId;
    private String orderId;
    private String reason;
    private Instant currentTime;

    public PaymentFailedEvent() {
    }

    public PaymentFailedEvent(String userId, String paymentId, String orderId, String reason) {
        this.userId = userId;
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.reason = reason;
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

    public String getReason() {
        return reason;
    }

    public Instant currentTime() {
        return null;
    }
}