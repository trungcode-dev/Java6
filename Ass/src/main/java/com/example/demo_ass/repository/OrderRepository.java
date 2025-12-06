package com.example.demo_ass.repository;

import com.example.demo_ass.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}