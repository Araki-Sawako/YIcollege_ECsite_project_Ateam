package com.college.yi.ecsite;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.college.yi.ecsite.entity.Order;
import com.college.yi.ecsite.front.service.OrderCompleteService;

public class OrderCompleteServiceTest {

    private OrderCompleteService orderCompleteService;
    private HttpSession mockSession;

    @BeforeEach
    void setUp() {
        orderCompleteService = new OrderCompleteService();
        mockSession = mock(HttpSession.class);
    }

    @Test
    void testGetOrderFromSession_ReturnsOrder() {
        // Arrange
        Order expectedOrder = new Order();
        when(mockSession.getAttribute("order")).thenReturn(expectedOrder);

        // Act
        Order actualOrder = orderCompleteService.getOrderFromSession(mockSession);

        // Assert
        assertSame(expectedOrder, actualOrder);
        verify(mockSession).getAttribute("order");
    }

    @Test
    void testRemoveOrderFromSession_RemovesOrder() {
        // Act
        orderCompleteService.removeOrderFromSession(mockSession);

        // Assert
        verify(mockSession).removeAttribute("order");
    }
}
