package com.example.hospital.service;

import com.example.hospital.model.Doctor;
import com.example.hospital.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepo;

    @Autowired
    public DoctorService(DoctorRepository doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    public Doctor save(Doctor doctor) {
        return doctorRepo.save(doctor);
    }

    public List<Doctor> findAll() {
        return doctorRepo.findAll();
    }

    public Optional<Doctor> findById(String id) {
        return doctorRepo.findById(id);
    }

    public void deleteById(String id) {
        doctorRepo.deleteById(id);
    }

    public boolean existsById(String id) {
        return doctorRepo.existsById(id);
    }
}