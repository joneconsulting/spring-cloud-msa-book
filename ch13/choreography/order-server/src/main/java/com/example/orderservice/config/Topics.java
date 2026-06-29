package com.example.orderservice.config;

public interface Topics {
    String ORDER_EVENT = "order-event";
    String ORDER_CANCEL_EVENT = "order-cancel-event";

    String PAYMENT_EVENT = "payment-event";
    String PAYMENT_FAILED_EVENT = "payment-failed-event";

    String PRODUCT_EVENT = "product-event";
    String PRODUCT_FAILED_EVENT = "product-failed-event";
}
