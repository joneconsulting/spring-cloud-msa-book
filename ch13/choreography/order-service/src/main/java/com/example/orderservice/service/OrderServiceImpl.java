package com.example.orderservice.service;

import com.example.orderservice.config.OrderStatus;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import com.example.saga.event.ProcessPaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;

    /* [ch13] */
    KafkaEventProducer kafkaEventProducer;

    /* [deleted] */
//    PaymentServiceClient paymentServiceClient;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            KafkaEventProducer kafkaEventProducer) {
        this.orderRepository = orderRepository;
        /* [ch13] */
        this.kafkaEventProducer = kafkaEventProducer;
        /* [deleted] */
//        this.paymentServiceClient = paymentServiceClient;
    }

    @Override
    public ProcessPaymentEvent createOrder(OrderDto orderDto) {
        log.info("Requested an order from {}", orderDto.getUserId());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);
        /* [ch13] */
        orderEntity.setStatus(OrderStatus.PENDING);

        orderRepository.save(orderEntity);

        // [ch13] Publish a ProcessPaymentEvent to notify other services
        ProcessPaymentEvent event = new ProcessPaymentEvent(
                orderEntity.getUserId(),
                orderEntity.getOrderId(), orderEntity.getProductId(),
                orderEntity.getQty(), orderEntity.getTotalPrice(),
                orderEntity.isSimulateCancel());

        kafkaEventProducer.processPaymentEvent(event);

        log.info("Order Created Successfully. Order ID: {}", orderEntity.getOrderId());

        return event;
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findByOrderId(orderId);
        OrderDto orderDto = new ModelMapper().map(orderEntity.get(), OrderDto.class);

        return orderDto;
    }

    @Override
    public Iterable<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
