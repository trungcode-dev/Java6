package com.example.lab7.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Products")
@Builder
public class Product {
    @Id
    private Integer id;
    private String name;
    private Double price;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
}
