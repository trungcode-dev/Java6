package com.example.lab7.service.impl;

import com.example.lab7.entity.Category;
import com.example.lab7.repository.CategoryRepository;
import com.example.lab7.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository repo;

    @Override
    public List<Category> findAll() {
        return repo.findAll();
    }

    @Override
    public Category findById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Category save(Category category) {
        return repo.save(category);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
