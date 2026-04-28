package com.example.saga.event;

import lombok.Getter;

import java.time.Instant;

@Getter
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
}