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
@Table(name="orders")
@Data
public class Order {
    private Long orderId;
    private Long userId;
    private BigDecimal totalAmount;
    private BigDecimal shippingFee;
    private BigDecimal taxAmount;
    private Long paymentMethodId;
    private Long addressId;
    private String addressMethods;  // 詳細なら別テーブル or JSON
    private String notes;
    private String orderStatus;     // Enum推奨
    private String paymentStatus;   // Enum推奨
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 主キーを追加

    private Long productId;
    private Integer quantity;
    

}
