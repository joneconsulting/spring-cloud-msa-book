package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentReceipt;
import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.jpa.PaymentRepository;
import com.example.paymentservice.model.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public PaymentReceipt createPayment(PaymentRequest request) {

        // 1) 기본 검증
        if (request.getOrderId() == null || request.getOrderId().isBlank()) {
            return new PaymentReceipt(null, "DECLINED", "orderId is required");
        }
        if (request.getUserId() == null || request.getUserId().isBlank()) {
            return new PaymentReceipt(null, "DECLINED", "userId is required");
        }
        if (request.getPaymentMethod() == null || request.getPaymentMethod().isBlank()) {
            return new PaymentReceipt(null, "DECLINED", "paymentMethod is required");
        }
        if (request.getAmount() == null || request.getAmount() <= 0) {
            return new PaymentReceipt(null, "DECLINED", "amount must be greater than 0");
        }

        // 2) 결제 생성 (실제 PG 연동 대신 DB 저장으로 대체)
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setAmount(request.getAmount());
        payment.setStatus("APPROVED"); // 실습용: 기본 승인

        Payment saved = paymentRepository.save(payment);

        // 3) 영수증 응답
        return new PaymentReceipt(saved.getPaymentId(), saved.getStatus(), "payment processed");
    }
}