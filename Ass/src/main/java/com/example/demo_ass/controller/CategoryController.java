package com.example.demo_ass.controller;

import com.example.demo_ass.dto.CategoryDTO;
import com.example.demo_ass.entity.Category;
import com.example.demo_ass.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return category == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(toDTO(category));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> create(@RequestBody Category category) {
        category.setId(null);
        Category saved = categoryService.save(category);
        return ResponseEntity.ok(toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody Category category) {
        if (categoryService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        category.setId(id);
        Category updated = categoryService.save(category);
        return ResponseEntity.ok(toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        if (!category.getProducts().isEmpty()) {
            return ResponseEntity.status(409).build();
        }

        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private CategoryDTO toDTO(Category c) {
        return new CategoryDTO(c.getId(), c.getName());
    }
}