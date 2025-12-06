package com.example.lab5.b4.service;

import java.util.List;

import com.example.lab5.b4.entity.Student;

public interface StudentService {
    List<Student> findAll();
    Student findById(String id);
    Student create(Student student);
    Student update(Student student);
    void deleteById(String id);
}

