package com.college.yi.ecsite.front.dto;

import java.math.BigDecimal;

import com.college.yi.ecsite.entity.CartItem;
import com.college.yi.ecsite.entity.Product;


//商品名、画像、単価、数量、小計などをひとまとめにして画面表示用の形式にする
public class CartItemDto {
	private Long productId;
	private String name;
	private String imageUrl;
	private BigDecimal unitPrice;
	private int quantity;
	private int subtotal;
	
	public CartItemDto(CartItem item, Product product) {
		this.productId=item.getProductId();
		this.name = product.getName();
        this.imageUrl = product.getImageUrl();
        this.unitPrice = product.getPrice();
        this.quantity = item.getQuantity();
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity)).intValue();

	}

	public Product getProduct() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
