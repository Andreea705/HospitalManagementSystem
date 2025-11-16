package com.example.hospital.repository;
import com.example.hospital.model.Department;
import com.example.hospital.repository.InFileRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentRepository extends InFileRepository<Department, String> {

    public DepartmentRepository() {
        super("departments.json", Department.class);
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
    protected String generateId() {

        return "HOSP_" + System.currentTimeMillis();
    }
}
