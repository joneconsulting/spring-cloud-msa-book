package com.example.paymentservice.service;

import com.example.paymentservice.config.Topics;
import com.example.saga.event.PaymentFailedEvent;
import com.example.saga.event.PaymentSucceededEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void successPaymentEvent(PaymentSucceededEvent event) {
        kafkaTemplate.send(Topics.PAYMENT_EVENT, event);
    }

    public void cancelPaymentEvent(PaymentFailedEvent event) {
        kafkaTemplate.send(Topics.PAYMENT_FAILED_EVENT, event);
    }
}

