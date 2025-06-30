package com.college.yi.ecsite.front.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.college.yi.ecsite.entity.Order;
import com.college.yi.ecsite.front.service.OrderCompleteService;

//注文完了画面
@Controller
@RequestMapping("/order/complete")
public class OrderCompleteController {
	
	private final OrderCompleteService orderCompleteService;

    public OrderCompleteController(OrderCompleteService orderCompleteService) {
        this.orderCompleteService = orderCompleteService;
    }

    @GetMapping
    public String showCompletePage(HttpSession session, Model model) {
        Order order = (Order) session.getAttribute("order"); //HttpSessionからorder情報を取得
        if (order == null) { //orderがない場合
            return "redirect:/order"; // 注文入力画面へ移動
        }
        model.addAttribute("order", order); //Modelにorderを渡す
        session.removeAttribute("order");
        return "order_complete"; // Thymeleafテンプレートを表示
    }

    @PostMapping("/top")
    public String goToTop() {
        return "redirect:/"; // 注文が完了したらトップページに戻る
    }
}
