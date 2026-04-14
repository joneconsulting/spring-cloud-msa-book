package com.example.orderservice.event;

import com.example.orderservice.config.OrderStatus;
import com.example.orderservice.config.Topics;
import com.example.orderservice.jpa.OrderRepository;
import com.example.orderservice.service.KafkaCommandProducer;
import com.example.saga.command.CancelPaymentCommand;
import com.example.saga.command.ReserveProductCommand;
import com.example.saga.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class OrderEventHandler {
    private OrderRepository orderRepository;
    private KafkaCommandProducer kafkaCommandProducer;

    public OrderEventHandler(OrderRepository orderRepository,
                             KafkaCommandProducer kafkaCommandProducer) {
        this.orderRepository = orderRepository;
        this.kafkaCommandProducer = kafkaCommandProducer;
    }

    @KafkaListener(topics = Topics.PAYMENT_EVENT, groupId = "order-service-group")
    @Transactional
    public void handlePaymentEvent(PaymentSucceededEvent event) {
        orderRepository.findByOrderId(event.getOrderId()).ifPresent(order -> {
            ReserveProductCommand command = new ReserveProductCommand(
                    event.getUserId(),
                    event.getOrderId(),
                    order.getProductId(),
                    event.getPaymentId(),
                    order.getQty()
            );
            kafkaCommandProducer.reserveProductCommand(command);

            order.setStatus(OrderStatus.PAID);

            log.info("Payment for order {} has been completed. Checking stock: {}", order.getId() , event.getOrderId());
        });
    }

    @KafkaListener(topics = Topics.PAYMENT_FAILED_EVENT, groupId = "order-service-group")
    @Transactional
    public void handlePaymentEvent(PaymentFailedEvent event) {
        orderRepository.findByOrderId(event.getOrderId()).ifPresent(order -> {
            order.setStatus(OrderStatus.CANCELLED);
            order.setReason(event.getReason());

            log.error("Order {} cancelled due to payment failure: {}", order.getId() , event.getReason());
        });
    }

    @KafkaListener(topics = Topics.PRODUCT_EVENT, groupId = "order-service-group")
    @Transactional
    public void handleProductEvent(ProductReservedEvent event) {
        orderRepository.findByOrderId(event.getOrderId()).ifPresent(order -> {
            order.setStatus(OrderStatus.COMPLETED);

            log.info("Order {} processing is completed. Shipping started: {}", order.getId());
        });
    }

    @KafkaListener(topics = Topics.PRODUCT_FAILED_EVENT, groupId = "order-service-group")
    @Transactional
    public void handleProductEvent(ProductReservationFailedEvent event) {
        orderRepository.findByOrderId(event.getOrderId()).ifPresent(order -> {
            // 재고 차감 실패 이벤트 수식
            CancelPaymentCommand command = new CancelPaymentCommand(order.getUserId(),
                    order.getOrderId(), event.getPaymentId(), event.getReason());       // "Insufficient stock"
            kafkaCommandProducer.cancelPaymentCommand(command);

            order.setStatus(OrderStatus.CANCELLED);
            order.setReason(event.getReason());

            log.error("Order {} was cancelled due to a lack of stock: {}", order.getId() , event.getOrderId());
        });
    }
}
