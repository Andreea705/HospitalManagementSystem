package com.example.hospital.controller;

import com.example.hospital.model.Department;
import com.example.hospital.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping //creaza un departament nou.
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department());
        return "department/form";
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "departments";
    }

    @GetMapping("/{id}/getById")
    public String getById(@PathVariable String id) {
        departmentService.getDepartmentById(id);
        return "departments:";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable String id, Model model) {
      var department = departmentService.getDepartmentById(id);
        model.addAttribute("department", department);
        return "department/form";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        departmentService.deleteDepartment(id);
        return "departments:";
    }
}

