package com.example.paymentservice.service;

import com.example.paymentservice.jpa.ProductEntity;
import com.example.paymentservice.jpa.ProductRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;

    Environment env;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, Environment env) {
        this.productRepository = productRepository;
        this.env = env;
    }

    @Override
    public Iterable<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }
}
