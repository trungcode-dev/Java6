package com.example.lab5.b3;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/students")
public class StudentRestApi {

    @GetMapping
    public Map<String, Student> findAll() {
        return Database.map;
    }

    @GetMapping("/{id}")
    public Student findById(@PathVariable String id) {
        return Database.map.get(id);
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        Database.map.put(student.getId(), student);
        return student;
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable String id, @RequestBody Student student) {
        Database.map.put(id, student);
        return student;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        Database.map.remove(id);
    }
}
