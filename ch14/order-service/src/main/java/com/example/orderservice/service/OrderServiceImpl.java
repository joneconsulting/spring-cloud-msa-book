package com.example.orderservice.service;

import com.example.orderservice.client.PaymentServiceClient;
import com.example.orderservice.config.OrderStatus;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import com.example.orderservice.vo.RequestPayment;
import com.example.orderservice.vo.ResponseOrder;
import com.example.orderservice.vo.ResponsePayment;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    PaymentServiceClient paymentServiceClient;
    PaymentQueryService paymentQueryService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, PaymentServiceClient paymentServiceClient, PaymentQueryService paymentQueryService) {
        this.orderRepository = orderRepository;
        this.paymentServiceClient = paymentServiceClient;
        this.paymentQueryService = paymentQueryService;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        log.info("Requested an order from {}", orderDto.getUserId());
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);
        orderEntity.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(orderEntity);

        RequestPayment requestPayment = new RequestPayment(orderDto.getOrderId()
                ,orderDto.getUserId(), orderDto.getTotalPrice(), "BANK");
        ResponsePayment responsePayment = paymentServiceClient.createPayment(requestPayment);

        OrderDto returnValue = mapper.map(orderEntity, OrderDto.class);
        returnValue.setPaymentMethod(responsePayment.getMethod());
        returnValue.setPaymentStatus(responsePayment.getStatus());
        returnValue.setPaymentDate(responsePayment.getCreatedAt());

        return returnValue;
    }

    @Override
    public List<OrderDto> getOrdersByUserId(String userId) {
        Iterable<OrderEntity> orderIt = orderRepository.findByUserId(userId);

        List<OrderDto> orderList = new ArrayList<>();
        orderIt.forEach(e -> {
            orderList.add(getOrderByOrderId(e.getOrderId()));
        });

        return orderList;
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findByOrderId(orderId);
        OrderDto returnValue = new ModelMapper().map(orderEntity.get(), OrderDto.class);

        CompletableFuture<ResponsePayment> responsePayment = paymentQueryService.getPaymentInfo(orderId);
        try {
            ResponsePayment payment = responsePayment.get();

            if (payment != null) {
                returnValue.setPaymentMethod(payment.getMethod());
                returnValue.setPaymentStatus(payment.getStatus());
                returnValue.setPaymentDate(payment.getCreatedAt());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        return returnValue;
    }
}
