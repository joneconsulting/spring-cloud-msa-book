package com.example.orderservice.config;

public interface Topics {
    String PAYMENT_COMMAND = "payment-command";
    String PAYMENT_CANCEL_COMMAND = "payment-cancel-command";
    String PAYMENT_EVENT = "payment-event";
    String PAYMENT_FAILED_EVENT = "payment-failed-event";

    String PRODUCT_COMMAND = "product-command";
    String PRODUCT_EVENT = "product-event";
    String PRODUCT_FAILED_EVENT = "product-failed-event";

//    String SHIPPING_COMPLETED = "shipping-completed";
//    String SHIPPING_FAILED = "shipping-failed";
}
