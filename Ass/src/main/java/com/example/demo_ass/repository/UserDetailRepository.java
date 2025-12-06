package com.example.demo_ass.repository;

import com.example.demo_ass.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<User, Long> {
}
