package com.example.ecommerce.service.impl;

import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.mapper.CartItemMapper;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartItemMapper cartItemMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    @Override
    public List<CartItem> getCartItems(Long userId) {
        return cartItemMapper.selectByUserId(userId);
    }
    
    @Override
    @Transactional
    public void addCartItem(Long userId, Long productId, Integer quantity) {
        CartItem existingItem = cartItemMapper.selectByUserIdAndProductId(userId, productId);
        
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setUpdateTime(LocalDateTime.now());
            cartItemMapper.updateById(existingItem);
        } else {
            Product product = productMapper.selectById(productId);
            if (product != null) {
                CartItem cartItem = new CartItem();
                cartItem.setUserId(userId);
                cartItem.setProductId(productId);
                cartItem.setProductName(product.getName());
                cartItem.setPrice(product.getPrice());
                cartItem.setQuantity(quantity);
                cartItem.setCreateTime(LocalDateTime.now());
                cartItem.setUpdateTime(LocalDateTime.now());
                cartItemMapper.insert(cartItem);
            }
        }
    }
    
    @Override
    @Transactional
    public void updateCartItem(Long userId, Long productId, Integer quantity) {
        CartItem cartItem = cartItemMapper.selectByUserIdAndProductId(userId, productId);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            cartItem.setUpdateTime(LocalDateTime.now());
            cartItemMapper.updateById(cartItem);
        }
    }
    
    @Override
    @Transactional
    public void deleteCartItem(Long userId, Long productId) {
        cartItemMapper.deleteByUserIdAndProductId(userId, productId);
    }
    
    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartItemMapper.deleteByUserId(userId);
    }
    
    @Override
    public int getCartCount(Long userId) {
        List<CartItem> items = cartItemMapper.selectByUserId(userId);
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }
}