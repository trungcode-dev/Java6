package com.example.demo_ass.service;

import com.example.demo_ass.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> findAll();
    List<OrderDetail> findByOrderId(Long orderId);
    OrderDetail findById(Long id);
    OrderDetail save(OrderDetail orderDetail);
    void delete(Long id);
}