package com.example.hospital.service;

import com.example.hospital.model.Hospital;
import com.example.hospital.repository.HospitalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public Hospital createHospital(Hospital hospital) {
        validateHospitalUniqueness(hospital, null); // Prüfen beim Erstellen
        return hospitalRepository.save(hospital);
    }

    // ============ READ METHODS (Unverändert) ============

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public Hospital getHospitalById(Long id) {
        return hospitalRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Hospital not found with id: " + id)
        );
    }

    // ============ UPDATE HOSPITAL ============

    public Hospital updateHospital(Long id, Hospital updatedHospital) {
        Hospital existingHospital = getHospitalById(id);

        validateHospitalUniqueness(updatedHospital, id);

        existingHospital.setName(updatedHospital.getName());
        existingHospital.setCity(updatedHospital.getCity());

        return hospitalRepository.save(existingHospital);
    }

    // ============ DELETE HOSPITAL ============

    public void deleteHospital(Long id) {
        Hospital hospital = getHospitalById(id);

        hospitalRepository.delete(hospital);
    }

    // ============ VALIDIERUNG ===========
    private void validateHospitalUniqueness(Hospital hospital, Long excludeId) {

        if (hospital.getName() == null || hospital.getName().trim().isEmpty()) {
            throw new RuntimeException("Hospital name must be provided.");
        }
        if (hospital.getCity() == null || hospital.getCity().trim().isEmpty()) {
            throw new RuntimeException("Hospital city must be provided.");
        }


        Hospital existingHospital = hospitalRepository.findByNameAndCity(
                hospital.getName(), hospital.getCity());

        if (existingHospital != null) {

            if (excludeId != null && existingHospital.getId().equals(excludeId)) {
                return;
            }
            throw new RuntimeException("A hospital named '" + hospital.getName() +
                    "' already exists in the city of '" + hospital.getCity() + "'.");
        }
    }
}