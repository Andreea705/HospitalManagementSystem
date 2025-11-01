package com.example.hospital.service;

import com.example.hospital.model.Department;
import com.example.hospital.repository.DepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    private final DepartmentRepo departmentRepo;

    @Autowired
    public DepartmentService(DepartmentRepo departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    public Department createDepartment(Department department) {
        return departmentRepo.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    public Department getDepartmentById(String id) {
        Optional<Department> department = departmentRepo.findById(id);
        if (department.isEmpty()) {
            throw new RuntimeException("Department not found with id: " + id);
        }
        return department.get();
    }

    public Department updateDepartment(String id, Department updatedDepartment) {
        Department existingDepartment = getDepartmentById(id);

        existingDepartment.setName(updatedDepartment.getName());
        existingDepartment.setHospitalId(updatedDepartment.getHospitalId());
        existingDepartment.setRoomNumbers(updatedDepartment.getRoomNumbers());
        existingDepartment.setDepartmentHead(updatedDepartment.getDepartmentHead());

        return departmentRepo.save(existingDepartment);
    }

    public boolean deleteDepartment(String id) {
        if (departmentRepo.existsById(id)) {
            departmentRepo.deleteById(id);
            return true;
        }
        return false;
    }
}