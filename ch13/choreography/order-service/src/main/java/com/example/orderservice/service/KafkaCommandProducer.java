package com.example.orderservice.service;

import com.example.orderservice.config.Topics;
import com.example.saga.command.CancelPaymentCommand;
import com.example.saga.command.ProcessPaymentCommand;
import com.example.saga.command.ReserveProductCommand;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaCommandProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaCommandProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void processPaymentCommand(ProcessPaymentCommand message) {
        kafkaTemplate.send(Topics.PAYMENT_COMMAND, message);
    }

    public void cancelPaymentCommand(CancelPaymentCommand message) {
        kafkaTemplate.send(Topics.PAYMENT_CANCEL_COMMAND, message);
    }

    public void reserveProductCommand(ReserveProductCommand message) {
        kafkaTemplate.send(Topics.PRODUCT_COMMAND, message);
    }
}

