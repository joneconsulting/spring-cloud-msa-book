package com.example.saga.event;

public record CancelPaymentEvent(
    String userId,
    String orderId,
    String paymentId,
    String reason
) {
}