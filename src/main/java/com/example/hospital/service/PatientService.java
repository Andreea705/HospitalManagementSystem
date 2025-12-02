package com.example.hospital.service;

import com.example.hospital.model.Patient;
import com.example.hospital.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // ============ CRUD Operations ============

    public Patient createPatient(Patient patient) {
        // Business validation: Check if patientId already exists
        if (patientRepository.existsByPatientId(patient.getPatientId())) {
            throw new RuntimeException("Patient with ID " + patient.getPatientId() + " already exists");
        }

        // Business validation: Check if email already exists
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new RuntimeException("Patient with email " + patient.getEmail() + " already exists");
        }

        // Set registration date
        patient.setRegistrationDate(LocalDateTime.now());

        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient existingPatient = getPatientById(id);

        // Business validation: Check if new patientId is unique (if changed)
        if (!existingPatient.getPatientId().equals(updatedPatient.getPatientId()) &&
                patientRepository.existsByPatientId(updatedPatient.getPatientId())) {
            throw new RuntimeException("Patient ID " + updatedPatient.getPatientId() + " already exists");
        }

        // Business validation: Check if new email is unique (if changed)
        if (!existingPatient.getEmail().equals(updatedPatient.getEmail()) &&
                patientRepository.existsByEmail(updatedPatient.getEmail())) {
            throw new RuntimeException("Email " + updatedPatient.getEmail() + " already exists");
        }

        existingPatient.setPatientId(updatedPatient.getPatientId());
        existingPatient.setName(updatedPatient.getName());
        existingPatient.setEmail(updatedPatient.getEmail());
        existingPatient.setPhoneNumber(updatedPatient.getPhoneNumber());
        existingPatient.setDateOfBirth(updatedPatient.getDateOfBirth());
        existingPatient.setEmergencyContact(updatedPatient.getEmergencyContact());

        return patientRepository.save(existingPatient);
    }

    public void deletePatient(Long id) {
        Patient patient = getPatientById(id);

//        // Business validation: Check if patient has active appointments
//        if (!patient.getAppointments().isEmpty()) {
//            throw new RuntimeException("Cannot delete patient with active appointments. Cancel appointments first.");
//        }

        patientRepository.deleteById(id);
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    public Optional<Patient> findPatientById(Long id) {
        return patientRepository.findById(id);
    }

    // ============ Business Logic Methods ============

    public Optional<Patient> getPatientByPatientId(String patientId) {
        return patientRepository.findByPatientId(patientId);
    }

    public boolean patientExists(Long id) {
        return patientRepository.existsById(id);
    }

    public boolean patientExistsByPatientId(String patientId) {
        return patientRepository.existsByPatientId(patientId);
    }

    public boolean patientExistsByEmail(String email) {
        return patientRepository.existsByEmail(email);
    }

    public List<Patient> searchPatientsByName(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name);
    }

//    public List<Patient> getPatientsByDepartment(Long departmentId) {
//        return patientRepository.findByDepartmentId(departmentId);
//    }

    public long countPatients() {
        return patientRepository.count();
    }

    public List<Patient> getPatientsRegisteredAfter(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        return patientRepository.findByRegistrationDateAfter(startOfDay);
    }

    // ============ Validation Methods ============

    public boolean isPatientIdAvailable(String patientId) {
        return !patientRepository.existsByPatientId(patientId);
    }

    public boolean isEmailAvailable(String email) {
        return !patientRepository.existsByEmail(email);
    }
}