package com.example.orderservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // 1) 기본(일반) RestClient.Builder  ★ Eureka 통신 등 고정 URL에 사용
    @Bean
    @Primary
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    // 2) 로드밸런싱 RestClient (SERVICE-NAME 호출용)
    @Bean
    @LoadBalanced
    public RestClient.Builder lbRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient lbRestClient(@Qualifier("lbRestClientBuilder") RestClient.Builder builder) {
        return builder.build();
    }
}
