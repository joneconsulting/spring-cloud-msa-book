package com.example.productservice.service;

import com.example.productservice.jpa.ProductRepository;
import com.example.productservice.model.Product;
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
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(String productId) {
        return productRepository.findByProductId(productId);
    }

    @Override
    public boolean updateProductStock(String productId, int quantity) {
        return productRepository.updateProductStock(productId, quantity) > 0;
    }
}
