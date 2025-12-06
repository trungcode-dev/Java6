package com.example.demo_ass.service.impl;

import com.example.demo_ass.entity.Order;
import com.example.demo_ass.entity.OrderDetail;
import com.example.demo_ass.entity.Product;
import com.example.demo_ass.entity.User;
import com.example.demo_ass.repository.OrderRepository;
import com.example.demo_ass.service.OrderService;
import com.example.demo_ass.service.ProductService;
import com.example.demo_ass.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Override
    public List<Order> findAll() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(order -> {
            if (order.getOrderDetails() == null) {
                order.setOrderDetails(new ArrayList<>());
            }
        });
        return orders;
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order save(Order order) {
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }
        if (order.getStatus() == null || order.getStatus().trim().isEmpty()) {
            order.setStatus("PENDING");
        }

        if (order.getOrderDetails() == null) {
            order.setOrderDetails(new ArrayList<>());
        }

        BigDecimal total = BigDecimal.ZERO;
        for (OrderDetail detail : order.getOrderDetails()) {
            Product product = productService.findById(detail.getProduct().getId());
            if (product != null && product.getPrice() != null) {
                detail.setUnitPrice(product.getPrice());
                total = total.add(product.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
            }
        }
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, String status) {
        Order order = findById(id);
        if (order != null) {
            order.setStatus(status.toUpperCase());
            orderRepository.save(order);
        }
    }
}