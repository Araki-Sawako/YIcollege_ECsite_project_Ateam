package com.college.yi.ecsite;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.college.yi.ecsite.entity.OderItem;
import com.college.yi.ecsite.entity.Order;
import com.college.yi.ecsite.entity.Product;
import com.college.yi.ecsite.front.repository.OrderMapper;
import com.college.yi.ecsite.front.repository.ProductMapper;
import com.college.yi.ecsite.front.service.OrderConfirmService;

public class OrderConfirmServiceTest {

    private ProductMapper productMapper;
    private OrderMapper orderMapper;
    private OrderConfirmService orderConfirmService;

    @BeforeEach
    void setUp() {
        productMapper = mock(ProductMapper.class);
        orderMapper = mock(OrderMapper.class);
        orderConfirmService = new OrderConfirmService(productMapper, orderMapper);
    }

    @Test
    void testCalculateTotalAmount() {
        OderItem item1 = new OderItem();
        item1.setPrice(new BigDecimal("100"));
        item1.setQuantity(2);

        OderItem item2 = new OderItem();
        item2.setPrice(new BigDecimal("150"));
        item2.setQuantity(1);

        List<OderItem> cart = Arrays.asList(item1, item2);
        BigDecimal result = orderConfirmService.calculateTotalAmount(cart);

        assertEquals(new BigDecimal("350"), result);
    }

    @Test
    void testCheckStock_returnsNullWhenStockIsEnough() {
        OderItem item = new OderItem();
        item.setProductId(1L);
        item.setQuantity(2);

        Product product = new Product();
        product.setStockQuantity(10);

        when(productMapper.findById(1L)).thenReturn(product);

        Product result = orderConfirmService.checkStock(List.of(item));

        assertNull(result);
    }

    @Test
    void testCheckStock_returnsProductWhenStockIsInsufficient() {
        OderItem item = new OderItem();
        item.setProductId(1L);
        item.setQuantity(5);

        Product product = new Product();
        product.setStockQuantity(3);

        when(productMapper.findById(1L)).thenReturn(product);

        Product result = orderConfirmService.checkStock(List.of(item));

        assertEquals(product, result);
    }

    @Test
    void testRegisterOrder() {
        Long userId = 10L;

        OderItem item = new OderItem();
        item.setProductId(1L);
        item.setQuantity(2);
        item.setPrice(new BigDecimal("100"));

        List<OderItem> cart = List.of(item);

        // モックされた動作
        doAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(99L); // IDを設定（DBで自動生成される想定）
            return null;
        }).when(orderMapper).insertOrder(any(Order.class));

        Order result = orderConfirmService.registerOrder(userId, cart);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(new BigDecimal("200"), result.getTotalAmount());
        assertEquals(99L, result.getOrderId());

        // 注文詳細と在庫更新の検証
        verify(orderMapper).insertOrderDetail(item);
        verify(productMapper).updateStock(1L, -2);
    }
}
