package com.example.notificationservice.service;

import com.example.notificationservice.jpa.ProductEntity;

public interface ProductService {
    Iterable<ProductEntity> getAllProducts();
}
