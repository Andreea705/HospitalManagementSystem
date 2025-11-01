package com.example.hospital.controller;

import com.example.hospital.model.Department;
import com.example.hospital.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class ControllerDepartment {

    private final DepartmentService service;

    @Autowired
    public ControllerDepartment(DepartmentService service ) {
        this.service = service;
    }

    @PostMapping
    public Department create(@RequestBody Department department) {
        return service.createDepartment(department);
    }

    @GetMapping
    public List<Department> getAll() {
        return service.getAllDepartments();
    }

    @GetMapping("/{id}")
    public Department getById(@PathVariable String id) {
        return service.getDepartmentById(id);
    }

    @PutMapping("/{id}")
    public Department update(@PathVariable String id, @RequestBody Department department) {
        return service.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteDepartment(id);
    }
}

