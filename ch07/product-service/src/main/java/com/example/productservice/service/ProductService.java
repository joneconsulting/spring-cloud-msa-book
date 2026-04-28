package com.example.productservice.service;

import com.example.productservice.model.Product;

public interface ProductService {
    Iterable<Product> getAllProducts();

    Product getProductById(String productId);

    boolean updateProductStock(String productId, int quantity);
}
