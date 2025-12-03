package com.example.hospital.service;

import com.example.hospital.model.MedicalStaffAppointment;
import com.example.hospital.repository.MedicalStaffAppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MedicalStaffAppointmentService {

    private final MedicalStaffAppointmentRepository repository;

    @Autowired
    public MedicalStaffAppointmentService(MedicalStaffAppointmentRepository repository) {
        this.repository = repository;
    }

    // ============ CREATE ============
    public MedicalStaffAppointment save(MedicalStaffAppointment msa) {
        // Validare: verifica daca relația exista deja
        if (repository.existsByMedicalStaffIdAndAppointmentId(
                msa.getMedicalStaffId(), msa.getAppointmentId())) {
            throw new RuntimeException("This assignment already exists");
        }
        return repository.save(msa);
    }

    // ============ READ ============
    public List<MedicalStaffAppointment> findAll() {
        return repository.findAll();
    }

    public MedicalStaffAppointment findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical staff appointment not found with id: " + id));
    }

    public List<MedicalStaffAppointment> findByMedicalStaffId(String medicalStaffId) {
        return repository.findByMedicalStaffId(medicalStaffId);
    }

    public List<MedicalStaffAppointment> findByAppointmentId(String appointmentId) {
        return repository.findByAppointmentId(appointmentId);
    }

    // ============ UPDATE ============
    public MedicalStaffAppointment update(Long id, MedicalStaffAppointment updated) {
        MedicalStaffAppointment existing = findById(id);

        existing.setMedicalStaffId(updated.getMedicalStaffId());
        existing.setAppointmentId(updated.getAppointmentId());

        return repository.save(existing);
    }

    // ============ DELETE ============
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Medical staff appointment not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public void deleteByMedicalStaffIdAndAppointmentId(String medicalStaffId, String appointmentId) {
        repository.findAll().stream()
                .filter(msa -> msa.getMedicalStaffId().equals(medicalStaffId)
                        && msa.getAppointmentId().equals(appointmentId))
                .findFirst()
                .ifPresent(msa -> repository.deleteById(msa.getId()));
    }

    // ============ VALIDARI ============
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public boolean existsByMedicalStaffIdAndAppointmentId(String medicalStaffId, String appointmentId) {
        return repository.existsByMedicalStaffIdAndAppointmentId(medicalStaffId, appointmentId);
    }

    public long count() {
        return repository.count();
    }
}