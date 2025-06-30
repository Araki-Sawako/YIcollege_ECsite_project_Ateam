package com.college.yi.ecsite.front.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.college.yi.ecsite.entity.OderItem;
import com.college.yi.ecsite.entity.Order;
import com.college.yi.ecsite.entity.Product;
import com.college.yi.ecsite.front.repository.OrderMapper;
import com.college.yi.ecsite.front.repository.ProductMapper;

@Service
public class OrderConfirmService {

    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;

    public OrderConfirmService(ProductMapper productMapper, OrderMapper orderMapper) {
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
    }

    // 合計金額を計算する
    public BigDecimal calculateTotalAmount(List<OderItem> cart) {
        return cart.stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 在庫不足の商品を返す（なければnull）
    public Product checkStock(List<OderItem> cart) {
        for (OderItem item : cart) {
            Product product = productMapper.findById(item.getProductId());
            if (product.getStockQuantity() < item.getQuantity()) {
                return product;
            }
        }
        return null; // 問題なければnull返却
    }
    // 注文登録と在庫更新（トランザクション管理）

    @Transactional
    public Order registerOrder(Long userId, List<OderItem> cart) {
        Order order = new Order();
        order.setUserId(userId);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(calculateTotalAmount(cart));

        orderMapper.insertOrder(order);

        for (OderItem item : cart) {
        	item.setOrderId(order.getOrderId());
            orderMapper.insertOrderDetail(item);
            productMapper.updateStock(item.getProductId(), -item.getQuantity());
        }
		return order;
    }
}
