package com.example.demo_ass.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CartItemDTO {
    private Long productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}