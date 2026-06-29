package com.example.orderservice.service;

import com.example.orderservice.config.Topics;
import com.example.saga.event.CancelPaymentEvent;
import com.example.saga.event.ProcessPaymentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void processPaymentEvent(ProcessPaymentEvent message) {
        kafkaTemplate.send(Topics.ORDER_EVENT, message);
    }

    public void cancelPaymentEvent(CancelPaymentEvent message) {
        kafkaTemplate.send(Topics.ORDER_CANCEL_EVENT, message);
    }
}

