package com.example.paymentservice.jpa;

import com.example.paymentservice.model.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
}
