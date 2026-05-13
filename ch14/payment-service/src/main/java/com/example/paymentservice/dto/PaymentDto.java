package com.example.paymentservice.dto;

import com.example.paymentservice.config.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto { // implements Serializable {
    private String paymentId;
    private Integer totalPrice;
    private String method;
    private PaymentStatus status;

    private String orderId;
    private String userId;
}
