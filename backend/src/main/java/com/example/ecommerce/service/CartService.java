package com.example.ecommerce.service;

import com.example.ecommerce.entity.CartItem;

import java.util.List;

public interface CartService {
    
    List<CartItem> getCartItems(Long userId);
    
    void addCartItem(Long userId, Long productId, Integer quantity);
    
    void updateCartItem(Long userId, Long productId, Integer quantity);
    
    void deleteCartItem(Long userId, Long productId);
    
    void clearCart(Long userId);
    
    int getCartCount(Long userId);
}