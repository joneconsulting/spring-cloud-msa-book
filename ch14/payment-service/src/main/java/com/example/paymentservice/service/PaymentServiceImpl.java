package com.example.paymentservice.service;

import com.example.paymentservice.config.PaymentStatus;
import com.example.paymentservice.dto.PaymentDto;
import com.example.paymentservice.jpa.PaymentEntity;
import com.example.paymentservice.jpa.PaymentRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Data
@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    PaymentRepository paymentRepository;

    Environment env;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, Environment env) {
        this.paymentRepository = paymentRepository;
        this.env = env;
    }

    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        paymentDto.setPaymentId(UUID.randomUUID().toString());
        paymentDto.setStatus(PaymentStatus.PAID);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PaymentEntity paymentEntity = mapper.map(paymentDto, PaymentEntity.class);

        paymentRepository.save(paymentEntity);

        PaymentDto returnValue = mapper.map(paymentEntity, PaymentDto.class);

        return returnValue;
    }

    @Override
    public Optional<PaymentEntity> getPaymentByPaymentId(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId);
    }

    @Override
    public Optional<PaymentEntity> getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}
