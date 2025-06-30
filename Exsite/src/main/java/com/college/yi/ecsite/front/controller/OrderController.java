package com.college.yi.ecsite.front.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.college.yi.ecsite.front.dto.OrderForm;

//注文手続き
@Controller
@RequestMapping("/order")
public class OrderController {
	
	@GetMapping
	public String showOrderForm(Model model) {  //注文入力フォームを表示
		model.addAttribute("orderForm", new OrderForm());
		return "front/order";
	}
	
	@PostMapping("/confirm")  //フォームを送信した際に、入力チェックを行う
	public String confirmOrder(
	        @ModelAttribute("orderForm") @Valid OrderForm form, //入力値をチェック
	        BindingResult result, 
	        HttpSession session
	) {
	    if (form.isSpecifyDatetime() && (form.getNote() == null || form.getNote().isEmpty())) {
	        result.rejectValue("note", "", "日時指定の場合、備考欄に入力してください。");
	    }

	    if (result.hasErrors()) { //エラーがあったら元の画面に戻す
	        return "front/order";
	    }

	    session.setAttribute("orderForm", form); //エラーがなければsessionにorderFormを保存→確認画面に遷移
	    return "redirect:/order/confirm";
	}	
	
	@GetMapping("/confirm") //確認画面を表示
	public String showConfirmPage(HttpSession session, Model model) {
		OrderForm form=(OrderForm) session.getAttribute("orderForm");
		if(form == null) { //入力フォームがからならば、注文入力画面にリダイレクト
			return "redirect:/order";
		}
		model.addAttribute("orderForm", form); //問題がない場合は確認画面へ遷移
		return "front/confirm";
	}
	

}
