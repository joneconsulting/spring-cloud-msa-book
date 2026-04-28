package com.example.orderservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Product { // implements Serializable {
    private Long id;
    private String productId;
    private String productName;
    private Integer stock;
    private Integer unitPrice;
    private Date createdAt;
}
