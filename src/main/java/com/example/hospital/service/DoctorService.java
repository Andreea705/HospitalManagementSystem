package com.example.hospital.service;

import com.example.hospital.model.Doctor;
import com.example.hospital.repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepo repoDoctor;

    @Autowired
    public DoctorService(DoctorRepo repoDoctor) {
        this.repoDoctor = repoDoctor;
    }

    public Doctor createDoctor(Doctor doctor) {
        validateDoctor(doctor);
        return repoDoctor.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return repoDoctor.findAllDoctors();
    }

    public Doctor getDoctorByLicenseNumber(String licenseNumber) {
        Doctor doctor = repoDoctor.findByLicenseNumber(licenseNumber);
        if (doctor == null) {
            throw new RuntimeException("Doctor not found with license number: " + licenseNumber);
        }
        return doctor;
    }

    public Doctor updateDoctor(String licenseNumber, Doctor updatedDoctor) {
        Doctor existingDoctor = getDoctorByLicenseNumber(licenseNumber);

        existingDoctor.setName(updatedDoctor.getName());
        existingDoctor.setDepartmentID(updatedDoctor.getDepartmentID());

        validateDoctor(existingDoctor);
        return repoDoctor.save(existingDoctor);
    }

    public boolean deleteDoctor(String licenseNumber) {
        return repoDoctor.deleteByLicenseNumber(licenseNumber);
    }


    private void validateDoctor(Doctor doctor) {
        if (doctor.getName() == null || doctor.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name cannot be empty");
        }
        if (doctor.getLicenseNumber() == null || doctor.getLicenseNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("License number cannot be empty");
        }
        if (doctor.getDepartmentID() == null || doctor.getDepartmentID().trim().isEmpty()) {
            throw new IllegalArgumentException("Department ID cannot be empty");
        }
    }


    public List<Doctor> getDoctorsByDepartment(String departmentId) {
        return repoDoctor.findByDepartmentID(departmentId);
    }

    public List<Doctor> findDoctorsByName(String name) {
        return getAllDoctors().stream()
                .filter(doctor -> doctor.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public boolean doctorExists(String licenseNumber) {
        return repoDoctor.findByLicenseNumber(licenseNumber) != null;
    }

    public long getTotalDoctorCount() {
        return getAllDoctors().size();
    }

    public long getDoctorCountByDepartment(String departmentId) {
        return getDoctorsByDepartment(departmentId).size();
    }


    public boolean isDoctorAvailable(String licenseNumber) {
        Doctor doctor = getDoctorByLicenseNumber(licenseNumber);
        return true;
    }


    public boolean transferDoctor(String licenseNumber, String newDepartmentId) {
        Doctor doctor = getDoctorByLicenseNumber(licenseNumber);
        doctor.setDepartmentID(newDepartmentId);
        repoDoctor.save(doctor);
        return true;
    }

}