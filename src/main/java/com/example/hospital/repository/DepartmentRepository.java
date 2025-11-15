package com.example.hospital.repository;

import com.example.hospital.model.Department;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class DepartmentRepository extends InFileRepository<Department, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public DepartmentRepository() {
        super("departments.json");
    }

    @Override
    protected Department convertToEntity(Object rawObject) {

        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) rawObject;

        return new Department(
                (String) map.get("id"),
                (String) map.get("name"),
                (String) map.get("hospitalId"),
                ((Number) map.get("roomNumbers")).intValue(),
                (String) map.get("departmentHead")
        );
    }

    @Override
    protected String getEntityId(Department department) {
        return department.getId();
    }

    @Override
    protected void setEntityId(Department department, String id) {
        department.setId(id);
    }

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected Long parseIdToLong(String id) {
        if (id != null && id.startsWith("DEPT_")) {
            try {
                return Long.parseLong(id.substring(5));
            } catch (NumberFormatException e) {
                return 0L;
            }
        }
        return 0L;
    }

    public List<Department> findByHospitalId(String hospitalId) {
        return findAll().stream()
                .filter(dept -> hospitalId.equals(dept.getHospitalId()))
                .collect(Collectors.toList());
    }
}
