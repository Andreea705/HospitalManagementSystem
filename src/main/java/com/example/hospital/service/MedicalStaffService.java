package com.example.hospital.service;

import com.example.hospital.model.MedicalStaff;
import com.example.hospital.repository.MedicalStaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalStaffService {

    private final MedicalStaffRepo medicalStaffRepo;

    @Autowired
    public MedicalStaffService(MedicalStaffRepo medicalStaffRepo) {
        this.medicalStaffRepo = medicalStaffRepo;
    }

    public MedicalStaff save(MedicalStaff medicalStaff) {
        return medicalStaffRepo.save(medicalStaff);
    }

    public List<MedicalStaff> findAll() {
        return medicalStaffRepo.findAll();
    }

    public Optional<MedicalStaff> findById(String id) {
        return medicalStaffRepo.findById(id);
    }

    public void deleteById(String id) {
        medicalStaffRepo.deleteById(id);
    }

    public boolean existsById(String id) {
        return medicalStaffRepo.existsById(id);
    }
}