package com.example.demo_ass.service.impl;

import com.example.demo_ass.entity.Product;
import com.example.demo_ass.entity.User;
import com.example.demo_ass.repository.ProductRepository;
import com.example.demo_ass.repository.UserDetailRepository;
import com.example.demo_ass.repository.UserRepository;
import com.example.demo_ass.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    UserDetailRepository repo;

    @Override
    public List<User> findAll() {
        return repo.findAll();
    }

    @Override
    public User findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public User save(User user) {
        return repo.save(user);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
