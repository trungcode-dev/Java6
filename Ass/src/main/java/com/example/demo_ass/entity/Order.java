package com.example.demo_ass.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "total_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    // SỬA TẠI ĐÂY: Đặt giá trị mặc định trong Java
    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";  // ← BẮT BUỘC CÓ DÒNG NÀY

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    // Đảm bảo orderDetails không null khi lưu
    @PrePersist
    @PreUpdate
    private void ensureOrderDetails() {
        if (this.orderDetails == null) {
            this.orderDetails = new ArrayList<>();
        }
    }
}