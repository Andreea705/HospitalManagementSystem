package com.example.hospital.service;

import com.example.hospital.model.Hospital;
import com.example.hospital.repository.HospitalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional  // WICHTIG: Für Datenbank-Transaktionen
public class HospitalService {

    // Benenne es besser um (nicht "Repo" sondern "Repository")
    private final HospitalRepository hospitalRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    // CREATE - ID wird automatisch von MySQL generiert
    public Hospital createHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    // READ ALL
    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    // UPDATE - Parameter ID von String zu Long ändern!
    public Hospital updateHospital(Long id, Hospital updatedHospital) {
        Hospital existingHospital = getHospitalById(id);

        existingHospital.setName(updatedHospital.getName());
        existingHospital.setCity(updatedHospital.getCity());

        return hospitalRepository.save(existingHospital);
    }


    public void deleteHospital(Long id) {
        if (!hospitalRepository.existsById(id)) {
            throw new RuntimeException("Hospital not found with id: " + id);
        }
        hospitalRepository.deleteById(id);
    }

    public Hospital getHospitalById(Long id) {
        Optional<Hospital> hospital = hospitalRepository.findById(id);

        return hospital.orElseThrow(() ->
                new RuntimeException("Hospital not found with id: " + id)
        );
    }

    public boolean hospitalExists(Long id) {
        return hospitalRepository.existsById(id);
    }

    // EXISTS BY NAME - Für Unique-Validierung
    public boolean hospitalExistsByName(String name) {
        return hospitalRepository.existsByName(name);
    }

    // COUNT
    public long countHospitals() {
        return hospitalRepository.count();
    }

    // Alternative: Get mit Optional (besser für Controller)
    public Optional<Hospital> findHospitalById(Long id) {
        return hospitalRepository.findById(id);
    }
}