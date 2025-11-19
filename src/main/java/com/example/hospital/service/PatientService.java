package com.example.hospital.service;

import com.example.hospital.model.Appointments;
import com.example.hospital.model.Patient;
import com.example.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepo;

    @Autowired
    public PatientService(PatientRepository patientRepo) {
        this.patientRepo = patientRepo;
    }

    public Patient createPatient(Patient patient) {
        return patientRepo.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    public Patient getPatientById(String id) {
        Optional<Patient> patient = patientRepo.findById(id);
        if (patient.isEmpty()) {
            throw new RuntimeException("Patient not found with id: " + id);
        }
        return patient.get();
    }

    public Patient updatePatient(String id, Patient updatedPatient) {
        Patient existingPatient = getPatientById(id);

        existingPatient.setName(updatedPatient.getName());
        existingPatient.setDateOfBirth(updatedPatient.getDateOfBirth());
        existingPatient.setGender(updatedPatient.getGender());
        existingPatient.setEmergencyContact(updatedPatient.getEmergencyContact());

        return patientRepo.save(existingPatient);
    }

    public void addAppointmentToPatient(String patientId, Appointments appointment) {
        Patient patient = getPatientById(patientId);
        patient.addAppointment(appointment);
        patientRepo.save(patient);
    }

    public void removeAppointmentFromPatient(String patientId, String appointmentId) {
        Patient patient = getPatientById(patientId);
        patient.getAppointments().removeIf(appt -> appt.getAppointmentId().equals(appointmentId));
        patientRepo.save(patient);
    }

    public List<Appointments> getPatientAppointments(String patientId) {
        Patient patient = getPatientById(patientId);
        return patient.getAppointments();

    }

    public boolean deletePatient(String id) {
        if (patientRepo.existsById(id)) {
            patientRepo.deleteById(id);
            return true;
        }
        return false;
    }
}

