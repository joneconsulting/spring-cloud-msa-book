package com.example.saga.event;

import java.time.Instant;

public class ProductReservationFailedEvent {
    private String orderId;
    private String productId;
    private String paymentId;
    private String reason;
    private Instant currentTime;

    public ProductReservationFailedEvent() {
    }

    public ProductReservationFailedEvent(String orderId, String productId, String paymentId, String reason) {
        this.orderId = orderId;
        this.productId = productId;
        this.paymentId = paymentId;
        this.reason = reason;
        this.currentTime = Instant.now();
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProductId() {
        return productId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getReason() {
        return reason;
    }

    public Instant currentTime() {
        return null;
    }
}