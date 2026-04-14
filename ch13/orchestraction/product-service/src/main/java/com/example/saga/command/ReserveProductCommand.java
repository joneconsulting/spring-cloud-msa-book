package com.example.saga.command;

public record ReserveProductCommand(
    String userId,
    String orderId,
    String productId,
    String paymentId,
    Integer qty
) {
}