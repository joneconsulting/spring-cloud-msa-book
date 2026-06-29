package com.example.orderservice.config;

public enum OrderStatus {
    PENDING("확인중"),
    PAID("결제완료"),
    CANCELLED("주문취소"),
    COMPLETED("주문완료")
    ;

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
