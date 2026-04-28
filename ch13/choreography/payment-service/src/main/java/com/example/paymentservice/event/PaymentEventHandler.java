package com.example.paymentservice.event;

import com.example.paymentservice.config.PaymentStatus;
import com.example.paymentservice.config.Topics;
import com.example.paymentservice.dto.PaymentDto;
import com.example.paymentservice.jpa.PaymentEntity;
import com.example.paymentservice.jpa.PaymentRepository;
import com.example.paymentservice.service.KafkaEventProducer;
import com.example.saga.event.CancelPaymentEvent;
import com.example.saga.event.PaymentFailedEvent;
import com.example.saga.event.PaymentSucceededEvent;
import com.example.saga.event.ProcessPaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Slf4j
public class PaymentEventHandler {
    private PaymentRepository paymentRepository;
    private KafkaEventProducer kafkaEventProducer;

    private ModelMapper mapper;

    public PaymentEventHandler(PaymentRepository paymentRepository,
                               KafkaEventProducer kafkaEventProducer) {
        this.paymentRepository = paymentRepository;
        this.kafkaEventProducer = kafkaEventProducer;

        mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @KafkaListener(topics = Topics.ORDER_EVENT, groupId = "order-service-group")
    @Transactional
    public void handleOrderEvent(ProcessPaymentEvent cmd) {
        if (cmd.totalPrice() > 100_000) { // 금액이 100,000원이 넘으로 잔액부족으로 오류 발생
            PaymentFailedEvent failedEvent = new PaymentFailedEvent(cmd.userId(), cmd.orderId(), "Insufficient balance");
            kafkaEventProducer.cancelPaymentEvent(failedEvent);

            log.error("Payment failed due to insufficient balance: {}", cmd.orderId());
        } else {
            PaymentDto paymentDto = new PaymentDto(
                    UUID.randomUUID().toString(),
                    cmd.totalPrice(),
                    "BANK",
                    PaymentStatus.PAID,
                    cmd.orderId(),
                    cmd.userId());

            PaymentEntity paymentEntity = mapper.map(paymentDto, PaymentEntity.class);
            paymentRepository.save(paymentEntity);

            PaymentSucceededEvent succeededEvent = new PaymentSucceededEvent(cmd.userId(), paymentEntity.getPaymentId(), cmd.orderId(), cmd.productId(), cmd.qty());
            kafkaEventProducer.successPaymentEvent(succeededEvent);

            log.info("Payment for the order Id {} was successful.", cmd.orderId());
        }
    }

    @KafkaListener(topics = Topics.ORDER_CANCEL_EVENT, groupId = "order-service-group")
    @Transactional
    public void handleOrderCancelEvent(CancelPaymentEvent cmd) {
        paymentRepository.findByPaymentId(cmd.paymentId()).ifPresent(payment -> {
            payment.setStatus(PaymentStatus.CANCELLED);
            payment.setReason(cmd.reason());

            log.error("Payment {} was canceled due to {}", cmd.paymentId() , cmd.reason());
        });
    }
}
