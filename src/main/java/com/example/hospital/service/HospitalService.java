package com.example.hospital.service;

import com.example.hospital.model.Hospital;
import com.example.hospital.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HospitalService {
    private final HospitalRepository hospitalRepo;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepo) {
        this.hospitalRepo = hospitalRepo;
    }

    public Hospital createHospital(Hospital hospital) {
        return hospitalRepo.save(hospital);
    }

    public List<Hospital> getAllHospitals() {
        return hospitalRepo.findAll();
    }

    public Hospital updateHospital(String id, Hospital updatedHospital) {
        Hospital existingHospital = getHospitalById(id);

        existingHospital.setName(updatedHospital.getName());
        existingHospital.setCity(updatedHospital.getCity());

        return hospitalRepo.save(existingHospital);
    }

    public boolean deleteHospital(String id) {
        if (hospitalRepo.existsById(id)) {
            hospitalRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public Hospital getHospitalById(String id) {
        Optional<Hospital> hospital = hospitalRepo.findById(id);
        if (hospital.isEmpty()) {
            throw new RuntimeException("Hospital not found with id: " + id);
        }
        return hospital.get();
    }

}

