package com.example.demo_ass.service.impl;

import com.example.demo_ass.entity.Category;
import com.example.demo_ass.repository.CategoryRepository;
import com.example.demo_ass.service.CategoryService;
import lombok.RequiredArgsConstructor;
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
    public Category findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Category save(Category category) {
        return repo.save(category);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
