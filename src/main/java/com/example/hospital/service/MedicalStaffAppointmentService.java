package com.example.hospital.service;

import com.example.hospital.model.MedicalStaffAppointment;
import com.example.hospital.repository.MedicalStaffAppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        // Validare  verifica daca relația exista deja, medicalstaff unic
        if(msa.getMedicalStaffId() != null && !msa.getMedicalStaffId().trim().isEmpty()) {
            List<MedicalStaffAppointment> existingList = repository.findByMedicalStaffId(msa.getMedicalStaffId());

            for(MedicalStaffAppointment existing : existingList) {
                throw  new RuntimeException("Medical Staff Already Exists");
            }
        }

        //Validare -appointmentId sa fie unic
        if(msa.getAppointmentId() != null && !msa.getAppointmentId().trim().isEmpty()) {
            List<MedicalStaffAppointment> existingList = repository.findByAppointmentId(msa.getAppointmentId());
            for(MedicalStaffAppointment existing : existingList) {
                throw  new RuntimeException("Appointment Already Exists");
            }
        }

        if (repository.existsByMedicalStaffIdAndAppointmentId(
                msa.getMedicalStaffId(), msa.getAppointmentId())) {
            throw new RuntimeException("This assignment already exists");
        }
        return repository.save(msa);
    }

    // ============ READ ============
//    public List<MedicalStaffAppointment> findAll() {
//        return repository.findAll();
//    }
//
    public MedicalStaffAppointment findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical staff appointment not found with id: " + id));
    }
    public List<MedicalStaffAppointment> findAll(String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();
        return repository.findAll(sort);
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

    // ============ FILTRARE ============
    public List<MedicalStaffAppointment> filter(String medicalStaffId, String appointmentId) {
        if ((medicalStaffId == null || medicalStaffId.isEmpty()) &&
                (appointmentId == null || appointmentId.isEmpty())) {
            return repository.findAll();
        }
        return repository.search(medicalStaffId, appointmentId);
    }

    // ============ FILTRARE + SORTARE ============
    public List<MedicalStaffAppointment> filterAndSort(String medicalStaffId,
                                                       String appointmentId,
                                                       String sortField,
                                                       String sortDirection) {
        List<MedicalStaffAppointment> result = filter(medicalStaffId, appointmentId);

        // Aplică sortarea manual
        if (sortField != null && !sortField.isEmpty()) {
            result.sort((a, b) -> {
                Comparable valA = getFieldValue(a, sortField);
                Comparable valB = getFieldValue(b, sortField);
                int cmp = valA.compareTo(valB);
                return sortDirection.equalsIgnoreCase("desc") ? -cmp : cmp;
            });
        }
        return result;
    }

    private Comparable getFieldValue(MedicalStaffAppointment msa, String field) {
        switch (field) {
            case "medicalStaffId":
                return msa.getMedicalStaffId();
            case "appointmentId":
                return msa.getAppointmentId();
            default:
                return msa.getId().toString();
        }
    }


    public long count() {
        return repository.count();
    }
}