package com.example.saga.command;

public record ProcessPaymentCommand(
    String userId,
    String orderId,
    String productId,
    String paymentId,
    int qty,
    int totalPrice,
    boolean simulateCancel
) {
}