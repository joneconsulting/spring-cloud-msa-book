package com.example.paymentservice.config;

public interface Topics {
    String PAYMENT_COMMAND = "payment-command";
    String PAYMENT_CANCEL_COMMAND = "payment-cancel-command";
    String PAYMENT_EVENT = "payment-event";
    String PAYMENT_FAILED_EVENT = "payment-failed-event";
}
