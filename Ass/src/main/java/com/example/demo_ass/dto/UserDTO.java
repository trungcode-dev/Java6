package com.example.demo_ass.dto;

public record UserDTO(
        Long id,
        String username,
        String fullName,
        String email,
        String phone,
        String address,
        String role
) {}