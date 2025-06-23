package com.college.yi.ecsite;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.college.yi.ecsite.entity.CartItem;
import com.college.yi.ecsite.entity.Product;
import com.college.yi.ecsite.front.dto.CartItemDto;
import com.college.yi.ecsite.front.repository.CartItemMapper;
import com.college.yi.ecsite.front.repository.ProductMapper;
import com.college.yi.ecsite.front.service.CartService;

	public class CartServiceTest {

	    @Mock
	    private CartItemMapper cartItemMapper;

	    @Mock
	    private ProductMapper productMapper;

	    @InjectMocks
	    private CartService cartService;

	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test //カート一覧が正しく表示されるか
	    public void testGetCartItems_ReturnsCorrectList() {
	        CartItem item = new CartItem();
	        item.setProductId(1L);
	        item.setQuantity(2);

	        Product product = new Product();
	        product.setId(1L);
	        product.setName("TestProduct");

	        when(cartItemMapper.findByUserId(100L)).thenReturn(List.of(item));
	        when(productMapper.findById(1L)).thenReturn(product);

	        List<CartItemDto> result = cartService.getCartItems(100L);

	        assertEquals(1, result.size());
	        assertEquals("TestProduct", result.get(0).getProduct().getName());
	    }
	    
	    @Test //数量変更が正しく行えるか
	    public void testUpdateQuantity_UpdatesItemQuantity() {
	        CartItem item = new CartItem();
	        item.setQuantity(1);

	        when(cartItemMapper.findByUserIdAndProductId(100L, 1L)).thenReturn(Optional.of(item));

	        cartService.updateQuantity(100L, 1L, 5);

	        assertEquals(5, item.getQuantity());
	        verify(cartItemMapper).update(item);
	    }

	    @Test //商品削除が実行されるか
	    public void testDeleteItem_DeletesCorrectItem() {
	        cartService.deleteItem(100L, 1L);
	        verify(cartItemMapper).delete(100L, 1L);
	    }
	    
	    @Test //商品追加が正常にできるか（新規のものに限る）
	    public void testAddToCart_InsertsNewItemWhenNotExists() {
	        when(cartItemMapper.findByUserIdAndProductId(100L, 1L)).thenReturn(Optional.empty());

	        cartService.addToCart(100L, 1L, 3);

	        verify(cartItemMapper).insert(any(CartItem.class));
	    }
	    
	    @Test //商品追加が正常にできるか（既存のものに限る）
	    public void testAddToCart_UpdatesQuantityWhenItemExists() {
	        CartItem item = new CartItem();
	        item.setQuantity(2);

	        when(cartItemMapper.findByUserIdAndProductId(100L, 1L)).thenReturn(Optional.of(item));

	        cartService.addToCart(100L, 1L, 3);

	        assertEquals(5, item.getQuantity());
	        verify(cartItemMapper).update(item);
	    }
	    
	    @Test //境界値（quantity=0）
	    public void testUpdateQuantity_ZeroQuantity() {
	        CartItem item = new CartItem();
	        item.setQuantity(1);

	        when(cartItemMapper.findByUserIdAndProductId(100L, 1L)).thenReturn(Optional.of(item));

	        cartService.updateQuantity(100L, 1L, 0);

	        assertEquals(0, item.getQuantity());
	        verify(cartItemMapper).update(item);
	    }
	    
	    @Test //cartが空の場合
	    public void testGetCartItems_WhenEmptyCart_ReturnsEmptyList() {
	        when(cartItemMapper.findByUserId(100L)).thenReturn(Collections.emptyList());

	        List<CartItemDto> result = cartService.getCartItems(100L);

	        assertTrue(result.isEmpty());
	    }
	    
	    @Test //エラー系
	    public void testGetCartItems_WhenProductIsNull_ThrowsException() {
	        CartItem item = new CartItem();
	        item.setProductId(1L);

	        when(cartItemMapper.findByUserId(100L)).thenReturn(List.of(item));
	        when(productMapper.findById(1L)).thenReturn(null);

	        assertThrows(NullPointerException.class, () -> {
	            cartService.getCartItems(100L);
	        });
	    }







	


}
