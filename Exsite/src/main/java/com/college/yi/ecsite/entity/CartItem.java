package com.college.yi.ecsite.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name="cart")
@Data
public class CartItem {
    private Long cartItemId;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主キー追加

    private String itemName;
    private int price;

}

