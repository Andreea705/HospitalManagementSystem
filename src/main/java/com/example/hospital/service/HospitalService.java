package com.example.hospital.service;

import com.example.hospital.model.Hospital;
import com.example.hospital.repository.HospitalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    // Modifizierte Methode für Listenansicht mit Filterung und Sortierung [cite: 43, 71]
    public List<Hospital> getAllHospitals(String name, String city, String sortField, String sortDir) {
        // Sortierung bestimmen: asc oder desc [cite: 46]
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        // Sicherstellen, dass Filter-Werte nicht null sind (für findByContaining) [cite: 74]
        String filterName = (name == null) ? "" : name;
        String filterCity = (city == null) ? "" : city;

        return hospitalRepository.findByNameContainingIgnoreCaseAndCityContainingIgnoreCase(
                filterName, filterCity, sort);
    }

    public Hospital createHospital(Hospital hospital) {
        validateHospitalUniqueness(hospital, null);
        return hospitalRepository.save(hospital);
    }

    public Hospital getHospitalById(Long id) {
        return hospitalRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Hospital not found with id: " + id)
        );
    }

    public Hospital updateHospital(Long id, Hospital updatedHospital) {
        Hospital existingHospital = getHospitalById(id);
        validateHospitalUniqueness(updatedHospital, id);
        existingHospital.setName(updatedHospital.getName());
        existingHospital.setCity(updatedHospital.getCity());
        return hospitalRepository.save(existingHospital);
    }

    public void deleteHospital(Long id) {
        Hospital hospital = getHospitalById(id);
        hospitalRepository.delete(hospital);
    }

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