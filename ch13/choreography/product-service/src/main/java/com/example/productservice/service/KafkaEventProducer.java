package com.example.productservice.service;

import com.example.productservice.config.Topics;
import com.example.saga.event.ProductReservationFailedEvent;
import com.example.saga.event.ProductReservedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void reservationSucceededEvent(ProductReservedEvent event) {
        kafkaTemplate.send(Topics.PRODUCT_EVENT, event);
    }

    public void reservationFailedEvent(ProductReservationFailedEvent event) {
        kafkaTemplate.send(Topics.PRODUCT_FAILED_EVENT, event);
    }
}

