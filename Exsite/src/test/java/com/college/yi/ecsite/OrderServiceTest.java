package com.college.yi.ecsite;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;

import com.college.yi.ecsite.front.dto.OrderForm;
import com.college.yi.ecsite.front.service.OrderService;

public class OrderServiceTest {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
    }

    @Test
    void validateOrderForm_日時指定trueかつ備考なしでエラー発生() {
        // Arrange
        OrderForm form = new OrderForm();
        form.setSpecifyDatetime(true);
        form.setNote(""); // 備考が空

        BindingResult result = mock(BindingResult.class);

        // Act
        orderService.validateOrderForm(form, result);

        // Assert
        verify(result).rejectValue("note", "", "日時指定の場合、備考欄に入力してください。");
    }

    @Test
    void validateOrderForm_日時指定falseなら備考が空でもエラーなし() {
        // Arrange
        OrderForm form = new OrderForm();
        form.setSpecifyDatetime(false);
        form.setNote("");

        BindingResult result = mock(BindingResult.class);

        // Act
        orderService.validateOrderForm(form, result);

        // Assert
        verify(result, never()).rejectValue(anyString(), anyString(), anyString());
    }

    @Test
    void saveOrderFormToSession_セッションに保存できる() {
        // Arrange
        HttpSession session = mock(HttpSession.class);
        OrderForm form = new OrderForm();
        form.setNote("テスト");

        // Act
        orderService.saveOrderFormToSession(form, session);

        // Assert
        verify(session).setAttribute("orderForm", form);
    }

    @Test
    void getOrderFormFromSession_セッションから取得できる() {
        // Arrange
        HttpSession session = mock(HttpSession.class);
        OrderForm form = new OrderForm();
        when(session.getAttribute("orderForm")).thenReturn(form);

        // Act
        OrderForm result = orderService.getOrderFormFromSession(session);

        // Assert
        assertEquals(form, result);
    }

    @Test
    void removeOrderFormFromSession_セッションから削除される() {
        // Arrange
        HttpSession session = mock(HttpSession.class);

        // Act
        orderService.removeOrderFormFromSession(session);

        // Assert
        verify(session).removeAttribute("orderForm");
    }
}
