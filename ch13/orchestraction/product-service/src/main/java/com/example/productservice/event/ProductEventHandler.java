package com.example.productservice.event;

import com.example.productservice.config.Topics;
import com.example.productservice.jpa.ProductRepository;
import com.example.productservice.service.KafkaCommandProducer;
import com.example.saga.command.ReserveProductCommand;
import com.example.saga.event.ProductReservationFailedEvent;
import com.example.saga.event.ProductReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class ProductEventHandler {
    private ProductRepository productRepository;
    private KafkaCommandProducer kafkaCommandProducer;

    private ModelMapper mapper;

    public ProductEventHandler(ProductRepository productRepository,
                               KafkaCommandProducer kafkaCommandProducer) {
        this.productRepository = productRepository;
        this.kafkaCommandProducer = kafkaCommandProducer;

        mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @KafkaListener(topics = Topics.PRODUCT_COMMAND, groupId = "order-service-group")
    @Transactional
    public void handleProductCommand(ReserveProductCommand cmd) {
        productRepository.findByProductId(cmd.productId()).ifPresent(product -> {
            if (product.getStock() - cmd.qty() < 0) {
                ProductReservationFailedEvent failedEvent = new ProductReservationFailedEvent(
                        cmd.orderId(), cmd.productId(), cmd.paymentId(), "Insufficient stock");
                kafkaCommandProducer.reservationFailedEvent(failedEvent);

                log.error("Order {} cannot be processed duto to insufficient stock: {}", cmd.productId(), cmd.productId());
            } else {
                product.setStock(product.getStock() - cmd.qty());

                ProductReservedEvent succeededEvent = new ProductReservedEvent(cmd.orderId(), cmd.productId());
                kafkaCommandProducer.reservationSucceededEvent(succeededEvent);

                log.info("Stock for the order {} has been successfully deducted: {}", cmd.orderId(), cmd.productId());
            }
        });
    }
}
