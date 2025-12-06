package com.example.demo_ass.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank
    private String username;

    @Column(nullable = false, length = 100)
    @NotBlank
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Email
    private String email;

    @Column(length = 15)
    private String phone;

    private String address;

    @Column(nullable = false, length = 20)
    private String role = "USER"; // USER, ADMIN
}