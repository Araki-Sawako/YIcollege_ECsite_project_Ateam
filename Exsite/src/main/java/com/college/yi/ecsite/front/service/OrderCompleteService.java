package com.college.yi.ecsite.front.service;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.college.yi.ecsite.entity.Order;

@Service
public class OrderCompleteService {

    private static final String ORDER_SESSION_KEY = "order";

    // セッションから注文情報を取得する
    public Order getOrderFromSession(HttpSession session) {
        return (Order) session.getAttribute(ORDER_SESSION_KEY);
    }

    // セッションから注文情報を削除する
    public void removeOrderFromSession(HttpSession session) {
        session.removeAttribute(ORDER_SESSION_KEY);
    }
}
