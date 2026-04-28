package com.example.saga.event;

import java.time.Instant;

public class PaymentSucceededEvent {
    private String userId;
    private String paymentId;
    private String orderId;
    private String productId;
    private Integer qty;
    private Instant currentTime;

    public PaymentSucceededEvent() {
    }

    public PaymentSucceededEvent(String userId, String paymentId, String orderId, String productId, Integer qty) {
        this.userId = userId;
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.productId = productId;
        this.qty = qty;
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

    public String getProductId() {
        return productId;
    }

    public Integer getQty() {
        return qty;
    }

    public Instant getCurrentTime() {
        return currentTime;
    }
}
