package com.example.orderservice.service;

import com.example.orderservice.dto.PaymentReceipt;
import com.example.orderservice.dto.PaymentRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.jpa.OrderRepository;
import com.example.orderservice.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    RestClient restClient;

    String productServiceUrl = "http://PRODUCT-SERVICE/product-service/products";
    String paymentServiceUrl = "http://PAYMENT-SERVICE/payment-service/payments";

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, @Qualifier("lbRestClient") RestClient restClient) {
        this.orderRepository = orderRepository;
        this.restClient = restClient;
    }

    @Override
    public Order createOrder(Order order) {
        // 1. 상품 재고 확인 및 확보
        String productId = order.getProductId();

        try {
            String retrieveStockUrl = productServiceUrl + "/" + productId;
            Product orderProduct = restClient
                    .get()
                    .uri(retrieveStockUrl)
                    .retrieve()
                    .body(Product.class);

            if (orderProduct.getStock() - order.getQty() < 0) {
                throw new IllegalStateException("재고 부족: 상품 ID = " + productId);
            }

            String updateStockUrl = productServiceUrl + "/" + productId + "/stock?quantity=-" + order.getQty();

            order.setOrderId(UUID.randomUUID().toString());
            ResponseEntity<Void> prodResponse = restClient
                    .put()
                    .uri(updateStockUrl)
                    .retrieve()
                    .toBodilessEntity();

            if (!prodResponse.getStatusCode().is2xxSuccessful()) {
                throw new IllegalStateException("주문 추가 중 상품 서비스에서 오류");
            }

            // 주문 생성
            order.setStatus("ORDER_COMPLETED");
            order.setOrderId(UUID.randomUUID().toString());
            order.setTotalPrice(order.getUnitPrice() * order.getQty());
        } catch (RestClientResponseException ex) {
            // 4xx/5xx 응답 등
            throw new IllegalStateException("재고 부족 또는 상품 서비스 오류", ex);
        }

        // 2. 결제 처리 요청
        PaymentRequest payReq = new PaymentRequest();
        payReq.setOrderId(order.getOrderId());
        payReq.setUserId(order.getUserId());
        payReq.setPaymentMethod("TRANSFER");
        payReq.setAmount(order.getTotalPrice());

        PaymentReceipt receipt;
        try {
            ResponseEntity<PaymentReceipt> payResponse = restClient
                    .post()
                    .uri(paymentServiceUrl)
                    .body(payReq)
                    .retrieve()
                    .toEntity(PaymentReceipt.class);

            if (!payResponse.getStatusCode().is2xxSuccessful() || payResponse.getBody() == null) {
                throw new IllegalStateException("결제 실패");
            }

            receipt = payResponse.getBody();
            if ("DECLINED".equals(receipt.getStatus())) {
                throw new IllegalStateException("결제 실패");
            }
        } catch (RestClientResponseException ex) {
            throw new IllegalStateException("결제 실패", ex);
        }

        // 3. 주문에 결제 ID 지정 및 저장
        order.setPaymentId(receipt.getPaymentId());

        // 저장 로직 (OrderRepository.save 등)
        orderRepository.save(order);

        return order;
    }

    @Override
    public Order getOrderByOrderId(String orderId) {
        Order order = orderRepository.findByOrderId(orderId);
//        Order order = new ModelMapper().map(orderEntity, Order.class);

        return order;
    }

    @Override
    public Iterable<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
