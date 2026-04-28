package com.example.productservice.controller;

import com.example.productservice.model.Product;
import com.example.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product-service")
public class ProductController {
    Environment env;
    ProductService productService;

    @Autowired
    public ProductController(Environment env, ProductService productService) {
        this.env = env;
        this.productService = productService;
    }

    // 전체 상품 목록 조회
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        Iterable<Product> iterable = productService.getAllProducts();

        List<Product> list = new ArrayList<>();
        for (Product item : iterable) {
            list.add(item);
        }

        // Java 8+ 의 경우 아래 방법으로 처리하셔도 됩니다.
        // iterable.iterator().forEachRemaining(list::add);

        return list;
    }

    // 상품 상세 조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable String productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 재고 감소 (또는 증가) 처리
    @PutMapping("/products/{productId}/stock")
    public ResponseEntity<Void> updateProductStock(@PathVariable String productId,
                                            @RequestParam int quantity) {
        boolean success = productService.updateProductStock(productId, quantity);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
