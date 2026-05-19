package com.example.orderservice.service;

import com.example.orderservice.client.PaymentServiceClient;
import com.example.orderservice.vo.ResponsePayment;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentQueryService {
    private final PaymentServiceClient paymentServiceClient;

//    @Retry(name = "paymentServiceRetry", fallbackMethod = "getPaymentInfoFallback")
//    @CircuitBreaker(name = "paymentServiceCircuitBreaker", fallbackMethod = "getPaymentInfoFallback")
//    @TimeLimiter(name = "paymentServiceTimeLimiter")  // CompletableFuture 반환 메서드에서만 타임아웃 적용
//    public CompletableFuture<ResponsePayment> getPaymentInfo(String orderId) {
//        log.info("Before call payment microservice: orderId [{}]", orderId);
//        ResponsePayment responsePayment = paymentServiceClient.getPayment(orderId);
//
//        log.info("After called payment microservice using open-feign");
//
//        return CompletableFuture.completedFuture(responsePayment);
//    }

    @Retry(name = "paymentServiceRetry", fallbackMethod = "getPaymentInfoFallback")
    @CircuitBreaker(name = "paymentServiceCircuitBreaker", fallbackMethod = "getPaymentInfoFallback")
    public ResponsePayment getPaymentInfo(String orderId) {
        log.info("Before call payment microservice: orderId [{}]", orderId);
        ResponsePayment responsePayment = paymentServiceClient.getPayment(orderId);

        log.info("After called payment microservice using open-feign");

        return responsePayment;
    }

//    public CompletableFuture<ResponsePayment> getPaymentInfoFallback(String orderId, Throwable ex) {
//        log.error("Fallback for payment: order_id: {}, {}", orderId, ex.getMessage());
//
//        ResponsePayment fallbackResponse = ResponsePayment.builder()
//                .orderId(orderId)
//                .status("PAYMENT_UNKNOWN")
//                .method(null)
//                .message("The payment service is temporarily unavailable.")
//                .build();
//
//        return CompletableFuture.completedFuture(fallbackResponse);
//    }
    public ResponsePayment getPaymentInfoFallback(String orderId, Throwable ex) {
        log.error("Fallback for payment: order_id: {}, {}", orderId, ex.getMessage());

        ResponsePayment fallbackResponse = ResponsePayment.builder()
                .orderId(orderId)
                .status("PAYMENT_UNKNOWN")
                .method(null)
                .message("The payment service is temporarily unavailable.")
                .build();

        return fallbackResponse;
    }

    /* bulkhead demo */
    @Bulkhead(name = "paymentServiceBulkhead",
              type = Bulkhead.Type.THREADPOOL,
              fallbackMethod = "bulkheadFallbackPayment")
    public CompletableFuture<String> bulkheadPaymentAsync(boolean simulateMode) throws InterruptedException {
        log.info("Processing payment test for bulkhead: simulateMode={}", simulateMode);

        if (simulateMode)
            Thread.sleep(5000); // 일부러 지연

        return CompletableFuture.completedFuture("Bulkhead test finished successfully.");
    }

    public CompletableFuture<String> bulkheadFallbackPayment(boolean simulateMode, Throwable t) {
        log.warn("Fallback method executed for bulkhead test: simulateMode={}, {}", simulateMode, t.getMessage());

        return CompletableFuture.completedFuture("Bulkhead test was conducted as a fallback method.");
    }
}
