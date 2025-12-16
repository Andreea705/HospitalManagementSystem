package com.example.hospital.service;

import com.example.hospital.model.Department;
import com.example.hospital.model.Hospital;
import com.example.hospital.repository.DepartmentRepository;
import com.example.hospital.repository.HospitalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // ============ CREATE ============

    public Department createDepartment(Department department, Long hospitalId) {

        validateDepartment(department, hospitalId, null);

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital not found with id: " + hospitalId));

        department.setHospital(hospital);
        return departmentRepository.save(department);
    }

    // ============ READ ============

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

    // ============ UPDATE ============

    public Department updateDepartment(Long id, Department departmentDetails, Long hospitalId) {
        Department department = getDepartmentById(id);

        Long targetHospitalId = hospitalId != null ? hospitalId : department.getHospital().getId();

        validateDepartment(departmentDetails, targetHospitalId, id);

        if (hospitalId != null && !department.getHospital().getId().equals(hospitalId)) {
            Hospital hospital = hospitalRepository.findById(hospitalId)
                    .orElseThrow(() -> new RuntimeException("Hospital not found with id: " + hospitalId));
            department.setHospital(hospital);
        }

        department.setName(departmentDetails.getName());
        department.setRoomNumbers(departmentDetails.getRoomNumbers());
        department.setDepartmentHead(departmentDetails.getDepartmentHead());

        return departmentRepository.save(department);
    }

    // ============ DELETE ============

    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);

        departmentRepository.delete(department);
    }

    // ============ BUSINESS LOGIC ============

    public Department increaseRoomNumbers(Long departmentId, int additionalRooms) {
        if (additionalRooms <= 0) {
            throw new RuntimeException("Additional rooms must be positive.");
        }

        Department department = getDepartmentById(departmentId);
        department.setRoomNumbers(department.getRoomNumbers() + additionalRooms);
        return departmentRepository.save(department);
    }

    public Department decreaseRoomNumbers(Long departmentId, int roomsToRemove) {
        if (roomsToRemove <= 0) {
            throw new RuntimeException("Rooms to remove must be positive.");
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

    // ============ ZENTRALE VALIDIERUNGSMETHODE ============


    // ============ ZENTRALE VALIDIERUNGSMETHODE ============

    private void validateDepartment(Department department, Long hospitalId, Long excludeId) {

        if (hospitalId == null) {
            throw new RuntimeException("A Hospital ID must be provided.");
        }
        if (!hospitalRepository.existsById(hospitalId)) {
            throw new RuntimeException("Hospital not found with id: " + hospitalId);
        }

        if (department.getRoomNumbers() < 0) {
            throw new RuntimeException("Room numbers cannot be negative.");
        }

        if (department.getName() != null && !department.getName().trim().isEmpty()) {

            Optional<Department> existingDepartments = departmentRepository.findByNameAndHospitalId(department.getName(), hospitalId);

            if (!existingDepartments.isEmpty()) {
                if (excludeId != null) {

                    boolean anotherDepartmentExistsWithSameName = existingDepartments.stream()
                            .anyMatch(dept -> !dept.getId().equals(excludeId));

                    if (anotherDepartmentExistsWithSameName) {
                        throw new RuntimeException(
                                "Department name '" + department.getName() +
                                        "' already exists in this hospital."
                        );
                    }

                } else {
                    throw new RuntimeException(
                            "Department name '" + department.getName() +
                                    "' already exists in this hospital."
                    );
                }
            }
        }
    }

    private boolean isDepartmentNameUniqueForUpdate(String name, Long hospitalId, Long excludeId) {
        return !departmentRepository.existsByNameAndHospitalId(name, hospitalId);
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
                .collect(Collectors.toList());
    }

    public List<Department> getFilteredAndSortedDepartments(
            Long hospitalId, String name, String head, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        String filterName = (name == null) ? "" : name;
        String filterHead = (head == null) ? "" : head;

        if (hospitalId != null) {
            return departmentRepository.findByHospital_IdAndNameContainingIgnoreCaseAndDepartmentHeadContainingIgnoreCase(
                    hospitalId, filterName, filterHead, sort);
        }
        return departmentRepository.findByNameContainingIgnoreCaseAndDepartmentHeadContainingIgnoreCase(
                filterName, filterHead, sort);
    }

    public long countDepartments() {
        return departmentRepository.count();
    }
}