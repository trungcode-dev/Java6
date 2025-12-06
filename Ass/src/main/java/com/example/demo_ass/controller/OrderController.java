package com.example.demo_ass.controller;

import com.example.demo_ass.dto.OrderDTO;
import com.example.demo_ass.dto.OrderDetailDTO;
import com.example.demo_ass.entity.Order;
import com.example.demo_ass.entity.OrderDetail;
import com.example.demo_ass.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDTO> getAll() {
        return orderService.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getById(@PathVariable Long id) {
        Order o = orderService.findById(id);
        return o == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(toDTO(o));
    }

    @PostMapping
    public ResponseEntity<OrderDTO> create(@RequestBody Order order) {
        order.setId(null);

        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }

        order.getOrderDetails().forEach(d -> d.setOrder(order));
        Order saved = orderService.save(order);
        return ResponseEntity.ok(toDTO(saved));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody String status) {
        if (orderService.findById(id) == null) return ResponseEntity.notFound().build();
        orderService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    private OrderDTO toDTO(Order o) {
        List<OrderDetailDTO> details = o.getOrderDetails().stream()
                .map(d -> new OrderDetailDTO(
                        d.getProduct().getId(),
                        d.getProduct().getName(),
                        d.getQuantity(),
                        d.getUnitPrice()
                ))
                .collect(Collectors.toList());

        return new OrderDTO(
                o.getId(),
                o.getUser().getId(),
                o.getUser().getUsername(),
                o.getOrderDate(),
                o.getTotalAmount(),
                o.getStatus(),
                details
        );
    }
}