package com.example.demo_ass.controller;

import com.example.demo_ass.dto.UserDTO;
import com.example.demo_ass.entity.User;
import com.example.demo_ass.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAll() {
        return userService.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        User user = userService.findById(id);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(toDTO(user));
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody User user) {
        user.setId(null);
        User saved = userService.save(user);
        return ResponseEntity.ok(toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody User user) {
        if (userService.findById(id) == null) return ResponseEntity.notFound().build();
        user.setId(id);
        User saved = userService.save(user);
        return ResponseEntity.ok(toDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (userService.findById(id) == null) return ResponseEntity.notFound().build();
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private UserDTO toDTO(User u) {
        return new UserDTO(
                u.getId(),
                u.getUsername(),
                u.getFullName(),
                u.getEmail(),
                u.getPhone(),
                u.getAddress(),
                u.getRole()
        );
    }
}
