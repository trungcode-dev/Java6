package com.example.demo_ass.service.impl;

import com.example.demo_ass.entity.Cart;
import com.example.demo_ass.entity.Product;
import com.example.demo_ass.entity.User;
import com.example.demo_ass.repository.CartRepository;
import com.example.demo_ass.repository.ProductRepository;
import com.example.demo_ass.repository.UserRepository;
import com.example.demo_ass.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Cart> getCart(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public void addItem(Long userId, Long productId, Integer quantity) {
        User user = getUser(userId);
        Product product = getProduct(productId);

        Cart cartItem = cartRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem == null) {
            cartItem = Cart.builder()
                    .user(user)
                    .product(product)
                    .quantity(0)
                    .build();
        }

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartRepository.save(cartItem);
    }

    @Override
    public void removeItem(Long userId, Long productId) {
        cartRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public void clearCart(Long userId) {
        cartRepository.deleteAll(cartRepository.findByUserId(userId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại!"));
    }
}