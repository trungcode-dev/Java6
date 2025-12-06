package com.example.lab7.service;

import com.example.lab7.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Integer id);
    Product save(Product product);
    void delete(Integer id);
}
