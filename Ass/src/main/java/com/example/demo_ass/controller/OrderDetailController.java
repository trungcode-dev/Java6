package com.example.demo_ass.controller;

import com.example.demo_ass.dto.OrderDetailDTO;
import com.example.demo_ass.entity.OrderDetail;
import com.example.demo_ass.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order-details")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @GetMapping
    public List<OrderDetailDTO> getAll() {
        return orderDetailService.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/order/{orderId}")
    public List<OrderDetailDTO> getByOrderId(@PathVariable Long orderId) {
        return orderDetailService.findByOrderId(orderId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDTO> getById(@PathVariable Long id) {
        OrderDetail od = orderDetailService.findById(id);
        return od == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(toDTO(od));
    }

    @PostMapping
    public ResponseEntity<OrderDetailDTO> create(@RequestBody OrderDetail orderDetail) {
        orderDetail.setId(null);
        OrderDetail saved = orderDetailService.save(orderDetail);
        return ResponseEntity.ok(toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetailDTO> update(@PathVariable Long id, @RequestBody OrderDetail orderDetail) {
        if (orderDetailService.findById(id) == null) return ResponseEntity.notFound().build();
        orderDetail.setId(id);
        OrderDetail saved = orderDetailService.save(orderDetail);
        return ResponseEntity.ok(toDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (orderDetailService.findById(id) == null) return ResponseEntity.notFound().build();
        orderDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private OrderDetailDTO toDTO(OrderDetail od) {
        return new OrderDetailDTO(
                od.getProduct().getId(),
                od.getProduct().getName(),
                od.getQuantity(),
                od.getUnitPrice()
        );
    }
}