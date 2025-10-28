package com.example.hospital.service;

import com.example.hospital.model.Department;
import com.example.hospital.model.Hospital;
import com.example.hospital.model.Room;
import com.example.hospital.repository.RepoHospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceHospital {
    private final RepoHospital repoHospital;

    @Autowired
    public ServiceHospital(RepoHospital repoHospital) {
        this.repoHospital = repoHospital;
    }


    public long getTotalHospitalCount() {
        return repoHospital.count();
    }


    private void validateHospital(Hospital hospital) {
        if (hospital.getName() == null || hospital.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Hospital name cannot be empty");
        }
        if (hospital.getCity() == null || hospital.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("Hospital city cannot be empty");
        }
        if (hospital.getId() == null || hospital.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Hospital ID cannot be empty");
        }
    }

    public Hospital getHospitalById(String id) {
        Hospital hospital = repoHospital.findById(id);
        if (hospital == null) {
            throw new RuntimeException("Hospital not found with id: " + id);
        }
        return hospital;
    }

    public List<Hospital> findHospitalsByCity(String city) {
        return repoHospital.findAll().stream()
                .filter(hospital -> hospital.getCity().equalsIgnoreCase(city))
                .toList();
    }

    public List<Hospital> searchHospitalsByName(String name) {
        return repoHospital.findAll().stream()
                .filter(hospital -> hospital.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }


    public boolean canAddDepartmentToHospital(String hospitalId) {
        Hospital hospital = getHospitalById(hospitalId);
        return hospital.getDepartments().size() < 20;
    }

    public boolean canAddRoomToHospital(String hospitalId) {
        Hospital hospital = getHospitalById(hospitalId);
        return hospital.getRooms().size() < 100;
    }


    public void addDepartmentToHospital(String hospitalId, Department department) {
        Hospital hospital = getHospitalById(hospitalId);
        if (canAddDepartmentToHospital(hospitalId)) {
            hospital.addDepartment(department);
            repoHospital.save(hospital);
        } else {
            throw new IllegalStateException("Cannot add more departments to hospital " + hospitalId);
        }
    }

    public void addRoomToHospital(String hospitalId, Room room) {
        Hospital hospital = getHospitalById(hospitalId);
        if (canAddRoomToHospital(hospitalId)) {
            hospital.addRoom(room);
            repoHospital.save(hospital);
        } else {
            throw new IllegalStateException("Cannot add more rooms to hospital " + hospitalId);
        }
    }

}

