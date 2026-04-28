package com.example.orderservice.service;

import com.example.orderservice.config.OrderStatus;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import com.example.saga.command.ProcessPaymentCommand;
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
    KafkaCommandProducer kafkaCommandProducer;

    /* [deleted] */
//    PaymentServiceClient paymentServiceClient;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            KafkaCommandProducer kafkaCommandProducer) {
        this.orderRepository = orderRepository;
        /* [ch13] */
        this.kafkaCommandProducer = kafkaCommandProducer;
        /* [deleted] */
//        this.paymentServiceClient = paymentServiceClient;
    }

    @Override
    public ProcessPaymentCommand createOrder(OrderDto orderDto) {
        log.info("Requested an order from {}", orderDto.getUserId());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);
        /* [ch13] */
        orderEntity.setStatus(OrderStatus.PENDING);

        orderRepository.save(orderEntity);

        // [ch13] Publish a ProcessPaymentCommand to notify other services
        ProcessPaymentCommand event = new ProcessPaymentCommand(
                orderEntity.getUserId(),
                orderEntity.getOrderId(), orderEntity.getProductId(),
                orderEntity.getQty(), orderEntity.getTotalPrice(),
                orderEntity.isSimulateCancel());

        kafkaCommandProducer.processPaymentCommand(event);

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
