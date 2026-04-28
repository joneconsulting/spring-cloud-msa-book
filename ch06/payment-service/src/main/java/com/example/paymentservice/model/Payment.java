package com.example.paymentservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String paymentId;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String paymentMethod; // CARD, TRANSFER, POINT 등

    @Column(nullable = false)
    private Integer amount; // 결제 금액(원) - 예시로 Integer 사용

    @Column(nullable = false)
    private String status; // APPROVED, DECLINED, CANCELLED 등

    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private Date createdAt;
}
