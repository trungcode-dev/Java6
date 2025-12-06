package com.example.demo_ass.service;

import com.example.demo_ass.dto.UserDTO;
import com.example.demo_ass.entity.Product;
import com.example.demo_ass.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAll();
    User findById(Long id);
    User save(User user);
    void delete(Long id);

}
