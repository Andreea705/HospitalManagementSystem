package com.example.hospital.service;

import com.example.hospital.model.Department;
import com.example.hospital.model.Hospital;
import com.example.hospital.model.Room;
import com.example.hospital.repository.HospitalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {
    private final HospitalRepo hospitalRepo;

    @Autowired
    public HospitalService(HospitalRepo hospitalRepo) {
        this.hospitalRepo = hospitalRepo;
    }

    public Hospital createHospital(Hospital hospital) {
        validateHospital(hospital);
        return hospitalRepo.save(hospital);
    }

    public List<Hospital> getAllHospitals() {
        return hospitalRepo.findAll();
    }

    public Hospital updateHospital(String id, Hospital updatedHospital) {
        Hospital existingHospital = getHospitalById(id);

        existingHospital.setName(updatedHospital.getName());
        existingHospital.setCity(updatedHospital.getCity());

        validateHospital(existingHospital);
        return hospitalRepo.save(existingHospital);
    }

    public boolean deleteHospital(String id) {
        Hospital hospital = hospitalRepo.findById(id);
        if (hospital != null) {
            return hospitalRepo.deleteById(id);
        }
        return false;
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
        Hospital hospital = hospitalRepo.findById(id);
        if (hospital == null) {
            throw new RuntimeException("Hospital not found with id: " + id);
        }
        return hospital;
    }

    public List<Hospital> findHospitalsByCity(String city) {
        return hospitalRepo.findAll().stream()
                .filter(hospital -> hospital.getCity().equalsIgnoreCase(city))
                .toList();
    }

    public List<Hospital> searchHospitalsByName(String name) {
        return hospitalRepo.findAll().stream()
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
            hospitalRepo.save(hospital);
        } else {
            throw new IllegalStateException("Cannot add more departments to hospital " + hospitalId);
        }
    }

    public void addRoomToHospital(String hospitalId, Room room) {
        Hospital hospital = getHospitalById(hospitalId);
        if (canAddRoomToHospital(hospitalId)) {
            hospital.addRoom(room);
            hospitalRepo.save(hospital);
        } else {
            throw new IllegalStateException("Cannot add more rooms to hospital " + hospitalId);
        }
    }

}

