package com.example.saga.event;

import java.time.Instant;

public class ProductReservedEvent {
    private String orderId;
    private String paymentId;
    private String productId;
    private Instant currentTime;

    public ProductReservedEvent() {
    }

    public ProductReservedEvent(String orderId, String productId) {
        this.orderId = orderId;
        this.productId = productId;
        this.currentTime = Instant.now();
    }

    public String getOrderId() {
        return orderId;
    }

    public Instant currentTime() {
        return null;
    }
}