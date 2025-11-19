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

    public Doctor updateDoctor(String id, Doctor updatedDoctor) {
        System.out.println("🔧 SERVICE UPDATE DOCTOR CALLED FOR ID: " + id);

        Optional<Doctor> optionalDoctor = findById(id);

        if (optionalDoctor.isPresent()) {
            Doctor existingDoctor = optionalDoctor.get();
            System.out.println("BEFORE UPDATE - Name: " + existingDoctor.getMedicalStaffName() +
                    ", Specialization: " + existingDoctor.getSpecialization());

            // Actualizează toate câmpurile
            existingDoctor.setMedicalStaffName(updatedDoctor.getMedicalStaffName());
            existingDoctor.setDepartmentID(updatedDoctor.getDepartmentID());
            existingDoctor.setLicenseNumber(updatedDoctor.getLicenseNumber());
            existingDoctor.setSpecialization(updatedDoctor.getSpecialization());

            Doctor saved = doctorRepo.save(existingDoctor);
            System.out.println("AFTER UPDATE - Name: " + saved.getMedicalStaffName() +
                    ", Specialization: " + saved.getSpecialization());
            System.out.println( "DOCTOR UPDATED SUCCESSFULLY");
            return saved;
        } else {
            System.out.println("DOCTOR NOT FOUND FOR UPDATE");
            return null;
        }
    }
}