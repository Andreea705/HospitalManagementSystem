package com.example.hospital.service;

import com.example.hospital.model.Department;
import com.example.hospital.repository.RepoDepartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceDepartment {
    private final RepoDepartment repoDepartment;

    @Autowired
    public ServiceDepartment(RepoDepartment repoDepartment) {
        this.repoDepartment = repoDepartment;
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
        return repoDepartment.findByHospitalId(hospitalId);
    }

    public boolean departmentExists(String id) {
        return repoDepartment.findById(id) != null;
    }

    public long getTotalDepartmentCount() {
        return repoDepartment.findAll().size();
    }

    public long getDepartmentCountByHospital(String hospitalId) {
        return repoDepartment.findByHospitalId(hospitalId).size();
    }


    public boolean hasAvailableCapacity(Department department) {
        return department.hasCapacity();
    }

    public List<Department> findDepartmentsWithCapacity(String hospitalId) {
        return repoDepartment.findByHospitalId(hospitalId).stream()
                .filter(Department::hasCapacity)
                .toList();
    }


    public List<Department> findDepartmentsByHead(String departmentHead) {
        return repoDepartment.findAll().stream()
                .filter(dept -> dept.getDepartmentHead().equalsIgnoreCase(departmentHead))
                .toList();
    }
}