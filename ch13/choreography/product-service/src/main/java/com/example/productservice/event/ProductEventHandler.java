package com.example.productservice.event;

import com.example.productservice.config.Topics;
import com.example.productservice.jpa.ProductRepository;
import com.example.productservice.service.KafkaCommandProducer;
import com.example.saga.event.PaymentSucceededEvent;
import com.example.saga.event.ProductReservationFailedEvent;
import com.example.saga.event.ProductReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class ProductEventHandler {
    private ProductRepository productRepository;
    private KafkaCommandProducer kafkaCommandProducer;

    public ProductEventHandler(ProductRepository productRepository,
                               KafkaCommandProducer kafkaCommandProducer) {
        this.productRepository = productRepository;
        this.kafkaCommandProducer = kafkaCommandProducer;
    }

    @KafkaListener(topics = Topics.PAYMENT_EVENT, groupId = "product-service-group")
    @Transactional
    public void handlePaymentSucceededEvent(PaymentSucceededEvent event) {
        productRepository.findByProductId(event.getProductId()).ifPresent(product -> {
            if (product.getStock() - event.getQty() < 0) {
                ProductReservationFailedEvent failedEvent = new ProductReservationFailedEvent(
                        event.getOrderId(), event.getProductId(), event.getPaymentId(), "Insufficient stock");
                kafkaCommandProducer.reservationFailedEvent(failedEvent);

                log.error("Order {} cannot be processed due to insufficient stock for product: {}",
                        event.getOrderId(), event.getProductId());
            } else {
                product.setStock(product.getStock() - event.getQty());

                ProductReservedEvent succeededEvent = new ProductReservedEvent(event.getOrderId(), event.getProductId());
                kafkaCommandProducer.reservationSucceededEvent(succeededEvent);

                log.info("Stock for order {} has been successfully deducted for product: {}",
                        event.getOrderId(), event.getProductId());
            }
        });
    }
}
