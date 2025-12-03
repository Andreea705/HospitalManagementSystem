package com.example.hospital.service;

import com.example.hospital.model.Nurse;
import com.example.hospital.model.QualificationLevel;
import com.example.hospital.model.Department;
import com.example.hospital.repository.NurseRepository;
import com.example.hospital.repository.DepartmentRepository;
import com.example.hospital.repository.AppointmentsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
        // Setează rolul automat
        nurse.setRole("nurse");

        // Setează departamentul dacă este specificat
        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
            nurse.setDepartment(department);
        }

        // Validări de unicitate
        validateNurseUniqueness(nurse, null);

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

    public List<Nurse> getNursesByQualification(QualificationLevel qualification) {
        return nurseRepository.findByQualificationLevel(qualification);
    }

    public List<Nurse> getNursesByShift(String shift) {
        return nurseRepository.findByShift(shift);
    }

    public List<Nurse> getNursesOnDuty(boolean onDuty) {
        return nurseRepository.findByOnDuty(onDuty);
    }

    public List<Nurse> searchNurses(String name, Long departmentId,
                                    QualificationLevel qualification,
                                    String shift, Boolean onDuty) {
        // Implementare bazată pe câmpuri - poți adapta după nevoie
        if (departmentId != null) {
            if (onDuty != null) {
                return nurseRepository.findByDepartment_IdAndOnDuty(departmentId, onDuty);
            }
            return nurseRepository.findByDepartment_Id(departmentId);
        }

        if (name != null && !name.isEmpty()) {
            return nurseRepository.findByMedicalStaffNameContainingIgnoreCase(name);
        }

        if (qualification != null) {
            return nurseRepository.findByQualificationLevel(qualification);
        }

        if (shift != null && !shift.isEmpty()) {
            return nurseRepository.findByShift(shift);
        }

        return getAllNurses();
    }

    public List<Nurse> getAvailableNurses() {
        return nurseRepository.findAvailableNurses();
    }

    public List<Nurse> getNursesWithoutDepartment() {
        return nurseRepository.findAll().stream()
                .filter(n -> n.getDepartment() == null)
                .toList();
    }

    // ============ UPDATE ============

    public Nurse updateNurse(Long id, Nurse nurseDetails, Long departmentId) {
        Nurse nurse = getNurseById(id);

        // Actualizează câmpurile de bază
        nurse.setMedicalStaffName(nurseDetails.getMedicalStaffName());
        nurse.setMedicalStaffId(nurseDetails.getMedicalStaffId());

        // Actualizează câmpurile specifice Nurse
        nurse.setQualificationLevel(nurseDetails.getQualificationLevel());
        nurse.setShift(nurseDetails.getShift());
        nurse.setOnDuty(nurseDetails.isOnDuty());

        // Actualizează departamentul
        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
            nurse.setDepartment(department);
        } else {
            nurse.setDepartment(null);
        }

        // Validări de unicitate
        validateNurseUniqueness(nurseDetails, id);

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

        // Verifică dacă asistanta are programări asignate
        if (nurse.hasAppointments()) {
            throw new RuntimeException(
                    "Cannot delete nurse with assigned appointments. " +
                            "Please reassign appointments first."
            );
        }

        nurseRepository.deleteById(id);
    }

    // ============ VALIDARE ============

    private void validateNurseUniqueness(Nurse nurse, Long excludeId) {
        // Verifică medicalStaffId unic
        if (nurse.getMedicalStaffId() != null && !nurse.getMedicalStaffId().isEmpty()) {
            boolean exists = nurseRepository.existsByMedicalStaffId(nurse.getMedicalStaffId());
            if (exists) {
                // Verifică dacă e aceeași înregistrare (în caz de update)
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
    }

    public boolean nurseExists(Long id) {
        return nurseRepository.existsById(id);
    }

    public boolean nurseExistsByMedicalStaffId(String medicalStaffId) {
        return nurseRepository.existsByMedicalStaffId(medicalStaffId);
    }

    public long countNursesByDepartment(Long departmentId) {
        return nurseRepository.countByDepartment_Id(departmentId);
    }

    public long countAllNurses() {
        return nurseRepository.count();
    }

    public long countNursesOnDuty() {
        return nurseRepository.countByOnDuty(true);
    }

    // ============ BUSINESS LOGIC ============

    public List<Nurse> getNursesByDepartmentAndShift(Long departmentId, String shift) {
        return nurseRepository.findByDepartment_IdAndShift(departmentId, shift);
    }

    public List<Nurse> getNursesByDepartmentAndQualification(Long departmentId, QualificationLevel qualification) {
        return nurseRepository.findByDepartment_IdAndQualificationLevel(departmentId, qualification);
    }

    public boolean canTakeMoreAppointments(Long nurseId) {
        Nurse nurse = getNurseById(nurseId);
        // Exemple de reguli de business:
        // 1. Trebuie să fie on duty
        // 2. Nu poate avea mai mult de X programări active
        return nurse.isOnDuty() && nurse.getActiveAppointmentCount() < 5;
    }
}