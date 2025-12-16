package com.example.hospital.service;

import com.example.hospital.model.Appointments;
import com.example.hospital.model.Patient;
import com.example.hospital.repository.AppointmentsRepository;
import com.example.hospital.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentsRepository appointmentsRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository, AppointmentsRepository appointmentsRepository) {
        this.patientRepository = patientRepository;
        this.appointmentsRepository = appointmentsRepository;
    }

    // ============ CRUD Operations ============

    public Patient createPatient(Patient patient) {
        //validare unicitatea id-ul pacientului
        if (patientRepository.existsByPatientId(patient.getPatientId())) {
            throw new RuntimeException("Patient with ID " + patient.getPatientId() + " already exists");
        }

        //validare unicitate e-mail pacient
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new RuntimeException("Patient with email " + patient.getEmail() + " already exists");
        }

        //seteaza data de inregistrare
        patient.setRegistrationDate(LocalDateTime.now());

        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient existingPatient = getPatientById(id);

        //validare - verifica daca pacientul s-a schimbat si daca e unic noul sau id
        if (!existingPatient.getPatientId().equals(updatedPatient.getPatientId()) &&
                patientRepository.existsByPatientId(updatedPatient.getPatientId())) {
            throw new RuntimeException("Patient ID " + updatedPatient.getPatientId() + " already exists");
        }

        // bussines validation - verifica daca e-mailul s-a schimbat si daca este unic
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


    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    public Optional<Patient> findPatientById(Long id) {
        return patientRepository.findById(id);
    }


    public void deletePatient(Long id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patientRepository.delete(patient);

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

    public List<Patient> getFilteredAndSortedPatients(String name, String email, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        String filterName = (name == null) ? "" : name;
        String filterEmail = (email == null) ? "" : email;

        return patientRepository.findByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(
                filterName, filterEmail, sort);
    }

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