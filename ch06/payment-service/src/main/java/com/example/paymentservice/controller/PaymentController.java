package com.example.paymentservice.controller;

import com.example.paymentservice.dto.PaymentReceipt;
import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment-service")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // order-service: POST http://PAYMENT-SERVICE/payment-service/payments
    @PostMapping("/payments")
    public ResponseEntity<PaymentReceipt> createPayment(@RequestBody PaymentRequest request) {
        PaymentReceipt receipt = paymentService.createPayment(request);

        // DECLINED면 200으로 내려도 되고(업무적 실패), 4xx로 내려도 됩니다.
        // order-service 예제는 status=DECLINED를 검사하므로 200 유지가 학습용으로 단순합니다.
        return ResponseEntity.ok(receipt);
    }
}