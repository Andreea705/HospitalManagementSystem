package com.example.hospital.repository;

import com.example.hospital.model.Department;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DepartmentRepository extends GenericRepository<Department, String> {

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected String getEntityId(Department department) {
        if (department.getId() == null || department.getId().isEmpty()) {
            return "DEPT_" + System.currentTimeMillis();
        }
        return department.getId();
    }

    public List<Department> findByHospitalId(String hospitalId) {
        return storage.values().stream()
                .filter(dept -> hospitalId.equals(dept.getHospitalId()))
                .collect(Collectors.toList());
    }
}

