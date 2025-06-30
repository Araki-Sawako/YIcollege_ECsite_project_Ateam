package com.college.yi.ecsite.front.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.college.yi.ecsite.entity.OderItem;
import com.college.yi.ecsite.entity.Order;
import com.college.yi.ecsite.entity.Product;
import com.college.yi.ecsite.front.repository.OrderMapper;
import com.college.yi.ecsite.front.repository.ProductMapper;

//確認画面
@Controller
@RequestMapping("/order/confirm-detail")
public class OrderConfirmController {
	

    @Autowired
    private ProductMapper productMapper; //DBから商品情報を取得・在庫を更新するためのMyBatisマッパー

    @Autowired
    private OrderMapper orderMapper; //注文情報をDBに登録するためのマッパー

 

    @PostMapping("/submit")  //ユーザーが注文を確定した処理を実行、注文確認画面に遷移
    public String confirmOrder(HttpSession session, RedirectAttributes redirectAttributes) {  //カート情報を取得
        List<OderItem> cart = (List<OderItem>) session.getAttribute("cart");

     // 在庫チェック
        for (OderItem item : cart) {
            Product product = productMapper.findById(item.getProductId());
            if (product.getStockQuantity() < item.getQuantity()) { //注文したい量より、在庫（ストック）の量が少ないとき
                redirectAttributes.addFlashAttribute("error", "在庫が不足しています：" + product.getName());
                return "redirect:/order/confirm";
            }
        }


        // 在庫更新・注文登録（注文したい量のストックがあった場合）
        Order order = new Order();
        order.setUserId((Long) session.getAttribute("userId"));  // ユーザーIDをセット（正しい）
        order.setCreatedAt(LocalDateTime.now());                 // 注文日時をセット（こちらが正しい）
        order.setTotalAmount(cart.stream()  //合計金額の計算を設定
            .map(d -> d.getPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add));  //	各商品ごとに 単価 × 数量 を BigDecimal で計算
        orderMapper.insertOrder(order);  //注文本体をDBに登録

        for (OderItem item : cart) { 
            item.setOrderId(order.getOrderId());
            orderMapper.insertOrderDetail(item);
            productMapper.updateStock(item.getProductId(), -item.getQuantity()); //該当商品の在庫を減らす
        }

        session.setAttribute("order", order); //セッションに注文情報をセットし、カート情報を削除
        session.removeAttribute("cart");
        return "redirect:/order/complete"; // 注文完了画面へ遷移
    }

    @PostMapping("/back") //注文入力画面へ戻るボタンの処理
    public String backToInput() {
        return "redirect:/order/input"; //注文画面に遷移
    }
}
