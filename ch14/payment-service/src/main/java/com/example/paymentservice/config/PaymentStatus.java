package com.example.paymentservice.config;

public enum PaymentStatus {
    PAID("결제완료"),
    CANCELLED("결제취소"),
    ;

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
