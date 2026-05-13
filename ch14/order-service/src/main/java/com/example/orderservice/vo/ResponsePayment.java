package com.example.orderservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ResponsePayment {
    private String paymentId;
    private String method;
    private String status;
    private Integer totalPrice;
    private String userId;
    private String orderId;
    private String message;
    private Date createdAt;
}
