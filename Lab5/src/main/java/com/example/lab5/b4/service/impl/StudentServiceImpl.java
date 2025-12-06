package com.example.lab5.b4.service.impl;


import java.util.List;

import com.example.lab5.b4.dao.StudentDAO;
import com.example.lab5.b4.entity.Student;
import com.example.lab5.b4.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentDAO dao;

    @Override
    public List<Student> findAll() {
        return dao.findAll();
    }

    @Override
    public Student findById(String id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    public Student create(Student student) {
        return dao.save(student);
    }

    @Override
    public Student update(Student student) {
        return dao.save(student);
    }

    @Override
    public void deleteById(String id) {
        dao.deleteById(id);
    }
}

