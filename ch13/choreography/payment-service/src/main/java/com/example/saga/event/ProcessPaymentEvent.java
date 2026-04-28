package com.example.saga.event;

public record ProcessPaymentEvent(
    String userId,
    String orderId,
    String productId,
    String paymentId,
    int qty,
    int totalPrice,
    boolean simulateCancel
) {
}