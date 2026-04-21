package com.example.paymentservice.service;

import com.example.paymentservice.config.Topics;
import com.example.saga.event.PaymentFailedEvent;
import com.example.saga.event.PaymentSucceededEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaCommandProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaCommandProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void successPaymentEvent(PaymentSucceededEvent event) {
        kafkaTemplate.send(Topics.PAYMENT_EVENT, event);
    }

    public void cancelPaymentCommand(PaymentFailedEvent event) {
        kafkaTemplate.send(Topics.PAYMENT_FAILED_EVENT, event);
    }
}

