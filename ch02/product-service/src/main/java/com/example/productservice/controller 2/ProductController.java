package com.example.productservice.controller;

import com.example.productservice.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class ProductController {
    // 임시로 세 개의 상품 리스트를 생성 (실제로는 DB 등을 조회)
    private List<Product> productList = Arrays.asList(
            new Product(1L, "상품A", 10000),
            new Product(2L, "상품B", 20000),
            new Product(3L, "상품C", 30000)
    );

    @GetMapping("/products")
    public List<Product> getProducts() {
        // 전체 상품 목록 반환 (JSON 형식으로 자동 변환되어 응답)
        return productList;
    }
}
