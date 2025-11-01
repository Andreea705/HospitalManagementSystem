package com.example.hospital.service;

import com.example.hospital.model.MedicalStaffAppointment;
import com.example.hospital.repository.MedicalStaffAppointmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalStaffAppointmentService {
    private final MedicalStaffAppointmentRepo medicalStaffAppointmentRepo;

    @Autowired
    public MedicalStaffAppointmentService(MedicalStaffAppointmentRepo medicalStaffAppointmentRepo) {
        this.medicalStaffAppointmentRepo = medicalStaffAppointmentRepo;
    }

    public MedicalStaffAppointment createMedicalStaffAppointment(MedicalStaffAppointment staffAppointment) {
        validateMedicalStaffAppointment(staffAppointment);
        return medicalStaffAppointmentRepo.save(staffAppointment);
    }

    public List<MedicalStaffAppointment> getAllMedicalStaffAppointments() {
        return medicalStaffAppointmentRepo.findByMedicalStaffAppointmentID(null);
    }

    public MedicalStaffAppointment getMedicalStaffAppointmentById(String id) {
        MedicalStaffAppointment appointment = medicalStaffAppointmentRepo.findByMedicalStaffAppointmentId(id);
        if (appointment == null) {
            throw new RuntimeException("Medical staff appointment not found with ID: " + id);
        }
        return appointment;
    }

    public MedicalStaffAppointment updateMedicalStaffAppointment(String id, MedicalStaffAppointment updatedAppointment) {
        MedicalStaffAppointment existingAppointment = getMedicalStaffAppointmentById(id);

        existingAppointment.setMedicalStaffId(updatedAppointment.getMedicalStaffId());
        existingAppointment.setAppointmentID(updatedAppointment.getAppointmentID());

        validateMedicalStaffAppointment(existingAppointment);
        return medicalStaffAppointmentRepo.save(existingAppointment);
    }

    public boolean deleteMedicalStaffAppointment(String id) {
        return medicalStaffAppointmentRepo.deleteByMedicalStaffAppointmentId(id);
    }

    private void validateMedicalStaffAppointment(MedicalStaffAppointment staffAppointment) {
        if (staffAppointment.getMedicalStaffAppointmentId() == null || staffAppointment.getMedicalStaffAppointmentId().trim().isEmpty()) {
            throw new IllegalArgumentException("Medical staff appointment ID cannot be empty");
        }
        if (staffAppointment.getMedicalStaffId() == null || staffAppointment.getMedicalStaffId().trim().isEmpty()) {
            throw new IllegalArgumentException("Medical staff ID cannot be empty");
        }
        if (staffAppointment.getAppointmentID() == null || staffAppointment.getAppointmentID().trim().isEmpty()) {
            throw new IllegalArgumentException("Appointment ID cannot be empty");
        }
    }


    public List<MedicalStaffAppointment> getAppointmentsByMedicalStaff(String medicalStaffId) {
        return medicalStaffAppointmentRepo.findByMedicalStaffId(medicalStaffId);
    }

    public List<MedicalStaffAppointment> getMedicalStaffByAppointment(String appointmentId) {
        return medicalStaffAppointmentRepo.findByAppointmentID(appointmentId);
    }

    public boolean medicalStaffAppointmentExists(String id) {
        return medicalStaffAppointmentRepo.existsByMedicalStaffAppointmentId(id);
    }

    public long getTotalMedicalStaffAppointmentCount() {
        return getAllMedicalStaffAppointments().size();
    }

    public boolean canAssignMedicalStaffToAppointment(String medicalStaffId, String appointmentId) {
        List<MedicalStaffAppointment> existingAssignments = getMedicalStaffByAppointment(appointmentId);
        boolean alreadyAssigned = existingAssignments.stream()
                .anyMatch(assignment -> medicalStaffId.equals(assignment.getMedicalStaffId()));

        return !alreadyAssigned;
    }

    public MedicalStaffAppointment assignMedicalStaffToAppointment(String medicalStaffId, String appointmentId) {
        if (!canAssignMedicalStaffToAppointment(medicalStaffId, appointmentId)) {
            throw new IllegalStateException("Medical staff is already assigned to this appointment");
        }

        MedicalStaffAppointment newAssignment = new MedicalStaffAppointment(
                generateMedicalStaffAppointmentId(medicalStaffId, appointmentId),
                medicalStaffId,
                appointmentId
        );

        return createMedicalStaffAppointment(newAssignment);
    }

    public boolean unassignMedicalStaffFromAppointment(String medicalStaffId, String appointmentId) {
        List<MedicalStaffAppointment> assignments = getAppointmentsByMedicalStaff(medicalStaffId).stream()
                .filter(assignment -> appointmentId.equals(assignment.getAppointmentID()))
                .toList();

        if (!assignments.isEmpty()) {
            return medicalStaffAppointmentRepo.deleteByMedicalStaffAppointmentId(assignments.get(0).getMedicalStaffAppointmentId());
        }
        return false;
    }

    public List<String> getMedicalStaffIdsByAppointment(String appointmentId) {
        return getMedicalStaffByAppointment(appointmentId).stream()
                .map(MedicalStaffAppointment::getMedicalStaffId)
                .toList();
    }

    public List<String> getAppointmentIdsByMedicalStaff(String medicalStaffId) {
        return getAppointmentsByMedicalStaff(medicalStaffId).stream()
                .map(MedicalStaffAppointment::getAppointmentID)
                .toList();
    }

    public boolean isMedicalStaffAvailableForAppointment(String medicalStaffId, String appointmentId) {
        List<MedicalStaffAppointment> staffAppointments = getAppointmentsByMedicalStaff(medicalStaffId);

        return staffAppointments.size() < 3 && canAssignMedicalStaffToAppointment(medicalStaffId, appointmentId);
    }


    private String generateMedicalStaffAppointmentId(String medicalStaffId, String appointmentId) {
        return "MSA_" + medicalStaffId + "_" + appointmentId;
    }


    public int assignMultipleMedicalStaffToAppointment(List<String> medicalStaffIds, String appointmentId) {
        int assignedCount = 0;
        for (String staffId : medicalStaffIds) {
            if (canAssignMedicalStaffToAppointment(staffId, appointmentId)) {
                assignMedicalStaffToAppointment(staffId, appointmentId);
                assignedCount++;
            }
        }
        return assignedCount;
    }

    public List<String> findAvailableMedicalStaffForAppointment(String appointmentId, List<String> potentialStaffIds) {
        return potentialStaffIds.stream()
                .filter(staffId -> isMedicalStaffAvailableForAppointment(staffId, appointmentId))
                .toList();
    }
}