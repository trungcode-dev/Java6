package com.example.demo_ass.service;

import com.example.demo_ass.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    void delete(Long id);
}
