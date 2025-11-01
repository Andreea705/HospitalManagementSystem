package com.example.hospital.service;

import com.example.hospital.model.Department;
import com.example.hospital.repository.DepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepo departmentRepo;
//aici sa fie facuta validarea cu setturi
    @Autowired
    public DepartmentService(DepartmentRepo departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    public Department createDepartment(Department department) {
        validateDepartment(department);
        return departmentRepo.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    public Department getDepartmentById(String id) {
        Department department = departmentRepo.findById(id);
        if (department == null) {
            throw new RuntimeException("Department not found with id: " + id);
        }
        return department;
    }

    public Department updateDepartment(String id, Department updatedDepartment) {
        Department existingDepartment = getDepartmentById(id);

        existingDepartment.setName(updatedDepartment.getName());
        existingDepartment.setHospitalId(updatedDepartment.getHospitalId());
        existingDepartment.setRoomNumbers(updatedDepartment.getRoomNumbers());
        existingDepartment.setDepartmentHead(updatedDepartment.getDepartmentHead());

        validateDepartment(existingDepartment);
        return departmentRepo.save(existingDepartment);
    }

    public boolean deleteDepartment(String id) {
        if (departmentRepo.findById(id) != null) {
            return departmentRepo.deleteById(id);
        }
        return false;
    }

    private void validateDepartment(Department department) {
        if (department.getName() == null || department.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be empty");
        }
        if (department.getHospitalId() == null || department.getHospitalId().trim().isEmpty()) {
            throw new IllegalArgumentException("Hospital ID cannot be empty");
        }
        if (department.getId() == null || department.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Department ID cannot be empty");
        }
        if (department.getRoomNumbers() < 0) {
            throw new IllegalArgumentException("Room numbers cannot be negative");
        }
    }

    public List<Department> getDepartmentsByHospital(String hospitalId) {
        return departmentRepo.findByHospitalId(hospitalId);
    }

    public boolean departmentExists(String id) {
        return departmentRepo.findById(id) != null;
    }

    public long getTotalDepartmentCount() {
        return departmentRepo.findAll().size();
    }

    public long getDepartmentCountByHospital(String hospitalId) {
        return departmentRepo.findByHospitalId(hospitalId).size();
    }


    public boolean hasAvailableCapacity(Department department) {
        return department.hasCapacity();
    }

    public List<Department> findDepartmentsWithCapacity(String hospitalId) {
        return departmentRepo.findByHospitalId(hospitalId).stream()
                .filter(Department::hasCapacity)
                .toList();
    }


    public List<Department> findDepartmentsByHead(String departmentHead) {
        return departmentRepo.findAll().stream()
                .filter(dept -> dept.getDepartmentHead().equalsIgnoreCase(departmentHead))
                .toList();
    }
}