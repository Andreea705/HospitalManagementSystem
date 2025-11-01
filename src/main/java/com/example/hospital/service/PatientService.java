package com.example.hospital.service;

import com.example.hospital.model.Patient;
import com.example.hospital.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepo patientRepo;

    @Autowired
    public PatientService(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    public Patient createPatient(Patient patient) {
        validatePatient(patient);
        return patientRepo.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    public Patient getPatientById(String id) {
        Patient patient = patientRepo.findById(id);
        if (patient == null) {
            throw new RuntimeException("Patient not found with id: " + id);
        }
        return patient;
    }

    public Patient updatePatient(String id, Patient updatedPatient) {
        Patient existingPatient = getPatientById(id);

        existingPatient.setName(updatedPatient.getName());
        existingPatient.setAge(updatedPatient.getAge());
        existingPatient.setGender(updatedPatient.getGender());
        existingPatient.setEmergencyContact(updatedPatient.getEmergencyContact());
        existingPatient.setAppointments(updatedPatient.getAppointments());

        validatePatient(existingPatient);
        return patientRepo.save(existingPatient);
    }

    public boolean deletePatient(String id) {
        if (patientRepo.findById(id) != null) {
            return patientRepo.deleteById(id);
        }
        return false;
    }

    private void validatePatient(Patient patient) {
        if (patient.getName() == null || patient.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name cannot be empty");
        }
        if (patient.getId() == null || patient.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient ID cannot be empty");
        }
        if (patient.getEmergencyContact() == null || patient.getEmergencyContact().trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact cannot be empty");
        }
    }

    public List<Patient> findPatientsByName(String name) {
        return getAllPatients().stream()
                .filter(patient -> patient.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }


    public boolean patientExists(String id) {
        return patientRepo.findById(id) != null;
    }

    public long getTotalPatientCount() {
        return getAllPatients().size();
    }

    public boolean canScheduleAppointment(String patientId) {
        Patient patient = getPatientById(patientId);

        long activeAppointments = patient.getAppointments().stream()
                .filter(appt -> "Active".equals(appt.getStatus()))
                .count();
        return activeAppointments < 5;
    }

    public List<Patient> findPatientsByEmergencyContact(String contact) {
        return getAllPatients().stream()
                .filter(patient -> contact.equals(patient.getEmergencyContact()))
                .toList();
    }

}