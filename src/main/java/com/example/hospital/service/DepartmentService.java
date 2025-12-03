package com.example.hospital.service;

import com.example.hospital.model.Department;
import com.example.hospital.model.Hospital;
import com.example.hospital.repository.DepartmentRepository;
import com.example.hospital.repository.HospitalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final HospitalRepository hospitalRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository,
                             HospitalRepository hospitalRepository) {
        this.departmentRepository = departmentRepository;
        this.hospitalRepository = hospitalRepository;
    }


    public Department createDepartment(Department department, Long hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital not found with id: " + hospitalId));

        if (departmentRepository.existsByNameAndHospitalId(department.getName(), hospitalId)) {
            throw new RuntimeException(
                    "Department '" + department.getName() +
                            "' already exists in hospital '" + hospital.getName() + "'"
            );
        }

        department.setHospital(hospital);
        return departmentRepository.save(department);
    }


    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    public Optional<Department> findDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    public List<Department> getDepartmentsByHospitalId(Long hospitalId) {
        return departmentRepository.findByHospitalId(hospitalId);
    }


    public Department updateDepartment(Long id, Department departmentDetails, Long hospitalId) {
        Department department = getDepartmentById(id);

        if (!department.getName().equals(departmentDetails.getName())) {
            Long currentHospitalId = department.getHospital().getId();
            Long targetHospitalId = hospitalId != null ? hospitalId : currentHospitalId;

            if (departmentRepository.existsByNameAndHospitalId(
                    departmentDetails.getName(), targetHospitalId)) {
                throw new RuntimeException(
                        "Department name '" + departmentDetails.getName() +
                                "' already exists in this hospital"
                );
            }
        }

        department.setName(departmentDetails.getName());
        department.setRoomNumbers(departmentDetails.getRoomNumbers());
        department.setDepartmentHead(departmentDetails.getDepartmentHead());

        // Update hospital if provided
        if (hospitalId != null) {
            Hospital hospital = hospitalRepository.findById(hospitalId)
                    .orElseThrow(() -> new RuntimeException("Hospital not found"));
            department.setHospital(hospital);
        }

        return departmentRepository.save(department);
    }


    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }


    public boolean departmentExists(Long id) {
        return departmentRepository.existsById(id);
    }

    public boolean departmentNameExistsInHospital(String name, Long hospitalId) {
        return departmentRepository.existsByNameAndHospitalId(name, hospitalId);
    }


    public boolean hasCapacity(Long departmentId) {
        Department department = getDepartmentById(departmentId);
        return department.hasCapacity();
    }

    public boolean isFull(Long departmentId) {
        Department department = getDepartmentById(departmentId);
        return department.isFull();
    }

    public int getAvailableCapacity(Long departmentId) {
        Department department = getDepartmentById(departmentId);
        return department.getAvailableCapacity();
    }


    public Department increaseRoomNumbers(Long departmentId, int additionalRooms) {
        if (additionalRooms <= 0) {
            throw new RuntimeException("Additional rooms must be positive");
        }

        Department department = getDepartmentById(departmentId);
        department.setRoomNumbers(department.getRoomNumbers() + additionalRooms);
        return departmentRepository.save(department);
    }

    public Department decreaseRoomNumbers(Long departmentId, int roomsToRemove) {
        if (roomsToRemove <= 0) {
            throw new RuntimeException("Rooms to remove must be positive");
        }

        Department department = getDepartmentById(departmentId);
        int newRoomCount = department.getRoomNumbers() - roomsToRemove;

        if (newRoomCount < 0) {
            throw new RuntimeException(
                    "Cannot remove " + roomsToRemove +
                            " rooms. Department only has " + department.getRoomNumbers()
            );
        }

        department.setRoomNumbers(newRoomCount);
        return departmentRepository.save(department);
    }

    public Department assignHead(Long departmentId, String newHead) {
        Department department = getDepartmentById(departmentId);
        department.setDepartmentHead(newHead);
        return departmentRepository.save(department);
    }


    public long countDepartments() {
        return departmentRepository.count();
    }


    public List<Department> findDepartmentsByCriteria(String name, String departmentHead,
                                                      Long hospitalId, Boolean hasCapacity) {
        List<Department> departments;

        if (hospitalId != null) {
            departments = departmentRepository.findByHospitalId(hospitalId);
        } else {
            departments = departmentRepository.findAll();
        }

        // Apply filters
        return departments.stream()
                .filter(dept -> name == null || dept.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(dept -> departmentHead == null ||
                        (dept.getDepartmentHead() != null &&
                                dept.getDepartmentHead().toLowerCase().contains(departmentHead.toLowerCase())))
                .filter(dept -> hasCapacity == null ||
                        (hasCapacity ? dept.hasCapacity() : dept.isFull()))
                .toList();
    }


}