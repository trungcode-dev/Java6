// src/main/java/com/example/demo_ass/controller/CartController.java
package com.example.demo_ass.controller;

import com.example.demo_ass.dto.CartItemDTO;
import com.example.demo_ass.dto.AddToCartRequest;
import com.example.demo_ass.entity.Cart;
import com.example.demo_ass.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private Long getCurrentUserId() {
        return 1L;
    }

    @GetMapping
    public List<CartItemDTO> getCart() {
        Long userId = getCurrentUserId();
        return cartService.getCart(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestBody AddToCartRequest req) {
        Long userId = getCurrentUserId();
        cartService.addItem(userId, req.getProductId(), req.getQuantity());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        cartService.removeItem(userId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        Long userId = getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }

    private CartItemDTO toDTO(Cart c) {
        return new CartItemDTO(
                c.getProduct().getId(),
                c.getProduct().getName(),
                c.getProduct().getPrice(),
                c.getQuantity()
        );
    }
}