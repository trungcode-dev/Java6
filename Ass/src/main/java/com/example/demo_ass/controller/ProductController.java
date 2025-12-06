package com.example.demo_ass.controller;

import com.example.demo_ass.dto.CategoryDTO;
import com.example.demo_ass.dto.ProductDTO;
import com.example.demo_ass.entity.Product;
import com.example.demo_ass.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        Product p = productService.findById(id);
        return p == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(toDTO(p));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody Product product) {
        product.setId(null);
        Product saved = productService.save(product);
        return ResponseEntity.ok(toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody Product product) {
        if (productService.findById(id) == null) return ResponseEntity.notFound().build();
        product.setId(id);
        Product saved = productService.save(product);
        return ResponseEntity.ok(toDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (productService.findById(id) == null) return ResponseEntity.notFound().build();
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ProductDTO toDTO(Product p) {
        return new ProductDTO(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getDescription(),
                p.getCategory() != null
                        ? new CategoryDTO(p.getCategory().getId(), p.getCategory().getName())
                        : null
        );
    }
}