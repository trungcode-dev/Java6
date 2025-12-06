package com.example.demo_ass.service;

import com.example.demo_ass.entity.Cart;
import java.util.List;

public interface CartService {
    List<Cart> getCart(Long userId);
    void addItem(Long userId, Long productId, Integer quantity);
    void removeItem(Long userId, Long productId);
    void clearCart(Long userId);
}