package com.example.hospital.repository;

import com.example.hospital.model.Department;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

//clasa generica de repository pe care sa o extinda restul claselor de repo
//schimb numele invers
@Repository
public class DepartmentRepo {
    private final List<Department> departments = new ArrayList<>();

    public Department save(Department department) {
        Department existingDepartment = findById(department.getId());
//op. generale pe lista, nu atr. spec.--interfata de repo si clasa generica de repo, restul extind clasa asta
        if (existingDepartment != null) {
            existingDepartment.setName(department.getName());
            existingDepartment.setHospitalId(department.getHospitalId());
            existingDepartment.setRoomNumbers(department.getRoomNumbers());
            existingDepartment.setDepartmentHead(department.getDepartmentHead());
            return existingDepartment;
        }
        else{
            departments.add(department);
            return department;
        }

    }

    public List<Department> findAll() {
        return new ArrayList<>(departments);
    }

    public Department findById(String id) {
        if (id == null) return null;

        for (Department department : departments) {
            if (department != null && id.equals(department.getId())) {
                return department;
            }
        }

        return null;
    }

    public boolean deleteById(String id) {
        Department department = findById(id);
        if (department != null) {
            return departments.remove(department);
        }

        return false;
    }

    public List<Department> findByHospitalId(String hospitalId) {
        List<Department> result = new ArrayList<>();
        for (Department department : departments) {
            if (department != null && hospitalId.equals(department.getHospitalId())) {
                result.add(department);
            }
        }

        return result;
    }

}

