package com.example.hospital.service;

import com.example.hospital.model.Nurse;
import com.example.hospital.model.QualificationLevel;
import com.example.hospital.model.Department;
import com.example.hospital.repository.NurseRepository;
import com.example.hospital.repository.DepartmentRepository;
import com.example.hospital.repository.AppointmentsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class NurseService {

    private final NurseRepository nurseRepository;
    private final DepartmentRepository departmentRepository;
    private final AppointmentsRepository appointmentRepository;

    @Autowired
    public NurseService(NurseRepository nurseRepository,
                        DepartmentRepository departmentRepository,
                        AppointmentsRepository appointmentRepository) {
        this.nurseRepository = nurseRepository;
        this.departmentRepository = departmentRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // ============ CREATE ============

    public Nurse createNurse(Nurse nurse, Long departmentId) {
        nurse.setRole("nurse");

        // Setează departamentul daca este specificat
        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
            nurse.setDepartment(department);
        }

        // Validari de unicitate
        validateNurseUniqueness(nurse, null, departmentId);

        return nurseRepository.save(nurse);
    }

    // ============ READ ============

    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }

    public Nurse getNurseById(Long id) {
        return nurseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nurse not found with id: " + id));
    }

    public Nurse getNurseByMedicalStaffId(String medicalStaffId) {
        return nurseRepository.findByMedicalStaffId(medicalStaffId)
                .orElseThrow(() -> new RuntimeException("Nurse not found with Medical Staff ID: " + medicalStaffId));
    }

    public List<Nurse> getNursesByDepartment(Long departmentId) {
        return nurseRepository.findByDepartment_Id(departmentId);
    }

//    public List<Nurse> searchNurses(String name, Long departmentId,
//                                    QualificationLevel qualification,
//                                    String shift, Boolean onDuty) {
//        // Implementare bazata pe campuri
//        if (departmentId != null) {
//            if (onDuty != null) {
//                return nurseRepository.findByDepartment_IdAndOnDuty(departmentId, onDuty);
//            }
//            return nurseRepository.findByDepartment_Id(departmentId);
//        }
//
//        if (name != null && !name.isEmpty()) {
//            return nurseRepository.findByMedicalStaffNameContainingIgnoreCase(name);
//        }
//
//        if (qualification != null) {
//            return nurseRepository.findByQualificationLevel(qualification);
//        }
//
//        if (shift != null && !shift.isEmpty()) {
//            return nurseRepository.findByShift(shift);
//        }
//
//        return getAllNurses();
//    }

    public List<Nurse> searchNurses(
            String name,
            Long departmentId,
            QualificationLevel qualification,
            String shift,
            Boolean onDuty,
            String sortBy,
            String sortDir) {

        // Normalize parameters
        String normalizedName = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
        String normalizedShift = (shift != null && !shift.trim().isEmpty()) ? shift.trim() : null;

        // Set default sorting if not provided
        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "medicalStaffName";
        }
        if (sortDir == null || sortDir.trim().isEmpty()) {
            sortDir = "asc";
        }

        Sort sort;

        // Handle special cases for sorting
        switch (sortBy.toLowerCase()) {
            case "department":
            case "department.name":
                // Pentru sortarea după departament, folosim order simplu
                // Nu avem nullsLast()/nullsFirst() în versiunea mai veche
                sort = sortDir.equalsIgnoreCase("desc")
                        ? Sort.by(Sort.Order.desc("department.name"))
                        : Sort.by(Sort.Order.asc("department.name"));
                break;

            case "name":
            case "medicalstaffname":
                sort = sortDir.equalsIgnoreCase("desc")
                        ? Sort.by("medicalStaffName").descending()
                        : Sort.by("medicalStaffName").ascending();
                break;

            case "qualification":
            case "qualificationlevel":
                sort = sortDir.equalsIgnoreCase("desc")
                        ? Sort.by("qualificationLevel").descending()
                        : Sort.by("qualificationLevel").ascending();
                break;

            case "shift":
                sort = sortDir.equalsIgnoreCase("desc")
                        ? Sort.by("shift").descending()
                        : Sort.by("shift").ascending();
                break;

            case "onduty":
            case "on_duty":
                sort = sortDir.equalsIgnoreCase("desc")
                        ? Sort.by("onDuty").descending()
                        : Sort.by("onDuty").ascending();
                break;

            default:
                sort = sortDir.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();
        }

        return nurseRepository.searchNurses(
                normalizedName,
                departmentId,
                qualification,
                normalizedShift,
                onDuty,
                sort
        );
    }

    // ============ UPDATE ============

    public Nurse updateNurse(Long id, Nurse nurseDetails, Long departmentId) {
        Nurse nurse = getNurseById(id);

        // Actualizeaza campurile
        nurse.setMedicalStaffName(nurseDetails.getMedicalStaffName());
        nurse.setMedicalStaffId(nurseDetails.getMedicalStaffId());

        // Actualizeaza campurile specifice Nurse
        nurse.setQualificationLevel(nurseDetails.getQualificationLevel());
        nurse.setShift(nurseDetails.getShift());
        nurse.setOnDuty(nurseDetails.isOnDuty());

        // Actualizeaza departamentul
        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
            nurse.setDepartment(department);
        } else {
            nurse.setDepartment(null);
        }

        // Validari de unicitate
        validateNurseUniqueness(nurseDetails, id, departmentId);

        return nurseRepository.save(nurse);
    }

    public Nurse toggleDutyStatus(Long id) {
        Nurse nurse = getNurseById(id);
        nurse.toggleDuty();
        return nurseRepository.save(nurse);
    }

    public Nurse changeShift(Long id, String newShift) {
        if (newShift == null || newShift.trim().isEmpty()) {
            throw new RuntimeException("Shift cannot be empty");
        }

        Nurse nurse = getNurseById(id);
        nurse.setShift(newShift);
        return nurseRepository.save(nurse);
    }

    public Nurse promoteNurse(Long id, QualificationLevel newQualification) {
        if (newQualification == null) {
            throw new RuntimeException("Qualification level cannot be null");
        }

        Nurse nurse = getNurseById(id);
        nurse.promote(newQualification);
        return nurseRepository.save(nurse);
    }

    public Nurse assignToDepartment(Long nurseId, Long departmentId) {
        Nurse nurse = getNurseById(nurseId);

        if (departmentId == null) {
            nurse.setDepartment(null);
        } else {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
            nurse.setDepartment(department);
        }

        return nurseRepository.save(nurse);
    }

    // ============ DELETE ============

    public void deleteNurse(Long id) {
        Nurse nurse = getNurseById(id);

        // Verifica daca asistanta are programari asignate
        if (nurse.hasAppointments()) {
            throw new RuntimeException(
                    "Cannot delete nurse with assigned appointments. " +
                            "Please reassign appointments first."
            );
        }

        nurseRepository.deleteById(id);
    }

    // ============ VALIDARE ============

    private void validateNurseUniqueness(Nurse nurse, Long excludeId, Long departmentId) {
        // Verifica medicalStaffId unic
        if (nurse.getMedicalStaffId() != null && !nurse.getMedicalStaffId().isEmpty()) {
            boolean exists = nurseRepository.existsByMedicalStaffId(nurse.getMedicalStaffId());
            if (exists) {
                // Verifica dacă e aceeași înregistrare (în caz de update)
                if (excludeId != null) {
                    Nurse existing = getNurseByMedicalStaffId(nurse.getMedicalStaffId());
                    if (!existing.getId().equals(excludeId)) {
                        throw new RuntimeException("Medical Staff ID '" + nurse.getMedicalStaffId() + "' already exists");
                    }
                } else {
                    throw new RuntimeException("Medical Staff ID '" + nurse.getMedicalStaffId() + "' already exists");
                }
            }
        }

        //verifica daca exista departamentul in baza de date
        if(departmentId != null){
            boolean departmentExists = departmentRepository.existsById(departmentId);
            if(!departmentExists){
                throw new RuntimeException("Department not found with id: " + departmentId);
            }
        }

        //validare - shift nu poate fi gol
        if(nurse.getShift() != null && nurse.getShift().trim().isEmpty()){
            throw new RuntimeException("Shift cannot be empty");
        }

        //validare -ualificationlevel valid
        if(nurse.getQualificationLevel() == null)
            throw new RuntimeException("Qualification level cannot be null");
    }

    public boolean nurseExistsByMedicalStaffId(String medicalStaffId) {
        return nurseRepository.existsByMedicalStaffId(medicalStaffId);
    }

    public long countAllNurses() {
        return nurseRepository.count();
    }
}