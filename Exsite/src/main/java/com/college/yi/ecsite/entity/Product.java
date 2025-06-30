package com.college.yi.ecsite.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name="products")
@Data
public class Product {
    private Long productId;
    private Long shopId;
    private Long categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal taxRate;
    private Integer stockQuantity;
    private String status; 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;




}
