package com.example.saga.command;

public record CancelPaymentCommand(
    String userId,
    String orderId,
    String paymentId,
    String reason
) {
}