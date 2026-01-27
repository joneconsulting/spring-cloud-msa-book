package com.example.productservice.jpa;

import com.example.productservice.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Product findByProductId(String productId);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stock = p.stock + :quantity WHERE p.productId = :productId")
    int updateProductStock(@Param("productId") String productId, @Param("quantity") int quantity);
}
