package com.example.productservice.model;

public class Product {
    private Long id;
    private String name;
    private int price;

    // 생성자
    public Product(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Getter
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }

    // Setter 생략

}
