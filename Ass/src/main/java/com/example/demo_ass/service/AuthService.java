package com.example.demo_ass.service;

import com.example.demo_ass.dto.RegisterRequest;
import com.example.demo_ass.entity.User;
import com.example.demo_ass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    public String register(RegisterRequest req) {
        if (req.getUsername() == null || req.getUsername().length() < 3) {
            return "Username phải ít nhất 3 ký tự!";
        }
        if (req.getPassword() == null || req.getPassword().length() < 8) {
            return "Mật khẩu phải ít nhất 8 ký tự!";
        }
        if (repo.existsByUsername(req.getUsername())) {
            return "Username đã tồn tại!";
        }
        if (repo.existsByEmail(req.getEmail())) {
            return "Email đã tồn tại!";
        }
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            return "Mật khẩu xác nhận không khớp!";
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setAddress(req.getAddress());
        user.setRole("USER");

        repo.save(user);
        return "OK";
    }
}