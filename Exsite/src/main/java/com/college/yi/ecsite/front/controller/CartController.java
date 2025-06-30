package com.college.yi.ecsite.front.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.college.yi.ecsite.front.dto.CartItemDto;
import com.college.yi.ecsite.front.service.CartService;

import lombok.Data;

@Controller
@RestController
@RequestMapping("/front/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@PostMapping("/add")
	public ResponseEntity<String> addToCart(
			@RequestBody AddToCartRequest request,
			HttpSession session
		){
		Long userId=(Long) session.getAttribute("userId");  //userIdを取得
		if(userId==null) { //Idがない場合
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ログインが必要です。");
		}
		
		String result=cartService.addToCart(userId, request.getProductId(),request.getQuantity()); //それぞれのカートに追加
		if(result!=null) { //取得したものが正常に追加出来た場合
			return ResponseEntity.badRequest().body(result);
		}
		
		return ResponseEntity.ok("カートに追加しました。");
	}

	//ここからカート追加等
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 初期表示
    @GetMapping
    public String showCart(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // ログイン画面へ
        }

        List<CartItemDto> cartItems = cartService.getCartItems(userId);
        model.addAttribute("cartItems", cartItems);
        return "front/cart"; // Thymeleafテンプレート名
    }

    // 数量変更
    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long productId,
                                  @RequestParam int quantity,
                                  HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        cartService.updateQuantity(userId, productId, quantity);
        return "redirect:/cart";
    }

    // 商品削除
    @PostMapping("/delete")
    public String deleteItem(@RequestParam Long productId,
                             HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        cartService.deleteItem(userId, productId);
        return "redirect:/cart";
    }

    // 「買い物を続ける」
    @PostMapping("/continue")
    public String continueShopping() {
        return "redirect:/"; // トップページ
    }

    // 「注文手続きへ進む」
    @PostMapping("/checkout")
    public String proceedToCheckout() {
        return "redirect:/order/confirm"; // 注文画面
    }
}

@Data
class AddToCartRequest{
	private Long productId;
	private int quantity;
}
