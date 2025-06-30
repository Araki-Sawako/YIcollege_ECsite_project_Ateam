package com.college.yi.ecsite.front.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.college.yi.ecsite.entity.CartItem;
import com.college.yi.ecsite.entity.Product;
import com.college.yi.ecsite.front.dto.CartItemDto;
import com.college.yi.ecsite.front.repository.CartItemMapper;
import com.college.yi.ecsite.front.repository.ProductMapper;

//ビジネスロジック
@Service
public class CartService {

    private final CartItemMapper cartMapper;
    private final ProductMapper productMapper;

    public CartService(CartItemMapper cartMapper, ProductMapper productMapper) {
        this.cartMapper = cartMapper;
        this.productMapper = productMapper;
    }

    public List<CartItemDto> getCartItems(Long userId) {  //指定したユーザーのカートに入っている商品情報を一覧で返す
        List<CartItem> items = cartMapper.findByUserId(userId);
        return items.stream().map(item -> {
            Product product = productMapper.findById(item.getProductId());
            return new CartItemDto(item, product);
        }).collect(Collectors.toList());
    }

    public void updateQuantity(Long userId, Long productId, int quantity) {  //指定したユーザーのカートの特定商品の数量を変更
        cartMapper.findByUserIdAndProductId(userId, productId).ifPresent(item -> {
            item.setQuantity(quantity);
            cartMapper.update(item);
        });
    }

    public void deleteItem(Long userId, Long productId) {  //指定したユーザーのカートから、指定の商品の削除
        cartMapper.delete(userId, productId);
    }

    public String addToCart(Long userId, Long productId, int quantity) {  //商品をカートに追加する
        cartMapper.findByUserIdAndProductId(userId, productId).ifPresentOrElse( //既に同じ商品がある場合は数量を加算、新規なら新しく挿入
            item -> {
                item.setQuantity(item.getQuantity() + quantity);
                cartMapper.update(item);
            },
            () -> {
                CartItem newItem = new CartItem();
                newItem.setUserId(userId);
                newItem.setProductId(productId);
                newItem.setQuantity(quantity);
                cartMapper.insert(newItem);
            }
        );
		return null;
    }
    
    public void updateQuantity1(Long userId, Long productId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("数量はマイナスにできません");
        }

        CartItemMapper cartItemMapper = null;
		Optional<CartItem> optionalItem = cartItemMapper.findByUserIdAndProductId(userId, productId);
        if (optionalItem.isPresent()) {
            CartItem item = optionalItem.get();
            item.setQuantity(quantity);
            cartItemMapper.update(item);
        }
    }

    public void clearCart(Long userId) {  //カート全削除メソッド（注文完了時に使用）
        cartMapper.deleteAllByUserId(userId); 
    }

}
