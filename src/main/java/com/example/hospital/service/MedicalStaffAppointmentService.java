package com.example.hospital.service;

import com.example.hospital.model.MedicalStaffAppointment;
import com.example.hospital.repository.MedicalStaffAppointmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalStaffAppointmentService {

    private final MedicalStaffAppointmentRepo medicalStaffAppointmentRepo;

    @Autowired
    public MedicalStaffAppointmentService(MedicalStaffAppointmentRepo medicalStaffAppointmentRepo) {
        this.medicalStaffAppointmentRepo = medicalStaffAppointmentRepo;
    }

    public MedicalStaffAppointment save(MedicalStaffAppointment medicalStaffAppointment) {
        return medicalStaffAppointmentRepo.save(medicalStaffAppointment);
    }

    public List<MedicalStaffAppointment> findAll() {
        return medicalStaffAppointmentRepo.findAll();
    }

    public Optional<MedicalStaffAppointment> findById(String id) {
        return medicalStaffAppointmentRepo.findById(id);
    }

    public void deleteById(String id) {
        medicalStaffAppointmentRepo.deleteById(id);
    }

    public boolean existsById(String id) {
        return medicalStaffAppointmentRepo.existsById(id);
    }

}