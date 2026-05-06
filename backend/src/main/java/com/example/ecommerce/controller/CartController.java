package com.example.ecommerce.controller;

import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @GetMapping("/items")
    public Result<List<CartItem>> getCartItems(@RequestParam Long userId) {
        List<CartItem> items = cartService.getCartItems(userId);
        return Result.success(items);
    }
    
    @PostMapping("/add")
    public Result<Void> addCartItem(@RequestParam Long userId, 
                                    @RequestParam Long productId, 
                                    @RequestParam(defaultValue = "1") Integer quantity) {
        cartService.addCartItem(userId, productId, quantity);
        return Result.success();
    }
    
    @PutMapping("/update")
    public Result<Void> updateCartItem(@RequestParam Long userId, 
                                       @RequestParam Long productId, 
                                       @RequestParam Integer quantity) {
        cartService.updateCartItem(userId, productId, quantity);
        return Result.success();
    }
    
    @DeleteMapping("/delete")
    public Result<Void> deleteCartItem(@RequestParam Long userId, @RequestParam Long productId) {
        cartService.deleteCartItem(userId, productId);
        return Result.success();
    }
    
    @DeleteMapping("/clear")
    public Result<Void> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return Result.success();
    }
    
    @GetMapping("/count")
    public Result<Map<String, Integer>> getCartCount(@RequestParam Long userId) {
        int count = cartService.getCartCount(userId);
        Map<String, Integer> result = new HashMap<>();
        result.put("count", count);
        return Result.success(result);
    }
}