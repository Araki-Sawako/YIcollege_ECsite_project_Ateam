package com.college.yi.ecsite.front.service;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.college.yi.ecsite.front.dto.OrderForm;

@Service
public class OrderService {

    private static final String ORDER_FORM_SESSION_KEY = "orderForm";

    //業務ルールに応じた入力チェック
    public void validateOrderForm(OrderForm form, BindingResult result) {
        if (form.isSpecifyDatetime() && (form.getNote() == null || form.getNote().isEmpty())) {
            result.rejectValue("note", "", "日時指定の場合、備考欄に入力してください。");
        }
    }

    // セッションにOrderFormを保存
    public void saveOrderFormToSession(OrderForm form, HttpSession session) {
        session.setAttribute(ORDER_FORM_SESSION_KEY, form);
    }

    // セッションからOrderFormを取得
    public OrderForm getOrderFormFromSession(HttpSession session) {
        return (OrderForm) session.getAttribute(ORDER_FORM_SESSION_KEY);
    }

    //セッションからOrderFormを削除
    public void removeOrderFormFromSession(HttpSession session) {
        session.removeAttribute(ORDER_FORM_SESSION_KEY);
    }
}
