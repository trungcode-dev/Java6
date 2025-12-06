package com.example.lab5.b4.dao;

import com.example.lab5.b4.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDAO extends JpaRepository<Student, String>{
}
