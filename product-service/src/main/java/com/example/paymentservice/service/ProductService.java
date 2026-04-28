package com.example.paymentservice.service;

import com.example.paymentservice.jpa.ProductEntity;

public interface ProductService {
    Iterable<ProductEntity> getAllProducts();
}
