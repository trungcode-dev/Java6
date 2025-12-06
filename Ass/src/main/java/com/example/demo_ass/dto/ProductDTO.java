package com.example.demo_ass.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductDTO(
        Long id,
        String name,
        BigDecimal price,
        String description,
        CategoryDTO category
) {}