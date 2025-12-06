package com.example.demo_ass.service;

import com.example.demo_ass.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> findAll();
    Order findById(Long id);
    Order save(Order order);
    void delete(Long id);
    void updateStatus(Long id, String status);
}