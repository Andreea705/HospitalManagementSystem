package com.example.hospital.repository;

import com.example.hospital.model.Patient;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Custom query methods
    Optional<Patient> findByPatientId(String patientId);

    boolean existsByPatientId(String patientId);

    boolean existsByEmail(String email);

    List<Patient> findByNameContainingIgnoreCase(String name);

    // Find patients registered after a certain date
    List<Patient> findByRegistrationDateAfter(LocalDateTime date);

    // Filterung nach Name UND Email
    List<Patient> findByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(
            String name, String email, Sort sort);

    void deleteByName(String name);
}