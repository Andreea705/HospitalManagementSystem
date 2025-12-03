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
        return hospitalRepository.save(hospital);
    }

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

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

    public boolean hospitalExistsByName(String name) {
        return hospitalRepository.existsByName(name);
    }


    public long countHospitals() {
        return hospitalRepository.count();
    }

    public Optional<Hospital> findHospitalById(Long id) {
        return hospitalRepository.findById(id);
    }
}