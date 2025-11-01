package com.example.hospital.service;

import com.example.hospital.model.MedicalStaff;
import com.example.hospital.repository.MedicalStaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalStaffService {
    private final MedicalStaffRepo repoMedicalStaff;

    @Autowired
    public MedicalStaffService(MedicalStaffRepo repoMedicalStaff) {
        this.repoMedicalStaff = repoMedicalStaff;
    }

    public MedicalStaff createMedicalStaff(MedicalStaff staff) {
        validateMedicalStaff(staff);
        return repoMedicalStaff.save(staff);
    }

    public List<MedicalStaff> getAllMedicalStaff() {
        return repoMedicalStaff.findAll();
    }

    public MedicalStaff getMedicalStaffById(String staffId) {
        MedicalStaff staff = repoMedicalStaff.findByMedicalStaffID(staffId);
        if (staff == null) {
            throw new RuntimeException("Medical staff not found with ID: " + staffId);
        }
        return staff;
    }

    public MedicalStaff updateMedicalStaff(String staffId, MedicalStaff updatedStaff) {
        MedicalStaff existingStaff = getMedicalStaffById(staffId);

        existingStaff.setMedicalStaffName(updatedStaff.getMedicalStaffName());
        existingStaff.setDepartamentID(updatedStaff.getDepartamentID());
        existingStaff.setRole(updatedStaff.getRole());
        existingStaff.setAppointments(updatedStaff.getAppointments());

        validateMedicalStaff(existingStaff);
        return repoMedicalStaff.save(existingStaff);
    }

    public boolean deleteMedicalStaff(String staffId) {
        return repoMedicalStaff.deleteByMedicalStaffID(staffId);
    }

    private void validateMedicalStaff(MedicalStaff staff) {
        if (staff.getMedicalStaffID() == null || staff.getMedicalStaffID().trim().isEmpty()) {
            throw new IllegalArgumentException("Medical staff ID cannot be empty");
        }
        if (staff.getMedicalStaffName() == null || staff.getMedicalStaffName().trim().isEmpty()) {
            throw new IllegalArgumentException("Medical staff name cannot be empty");
        }
        if (staff.getDepartamentID() == null || staff.getDepartamentID().trim().isEmpty()) {
            throw new IllegalArgumentException("Department ID cannot be empty");
        }
        if (staff.getRole() == null || staff.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty");
        }
    }

    public List<MedicalStaff> getMedicalStaffByDepartment(String departmentId) {
        return repoMedicalStaff.findByDepartamentID(departmentId);
    }

    public List<MedicalStaff> getMedicalStaffByRole(String role) {
        return repoMedicalStaff.findByRole(role);
    }

    public List<MedicalStaff> findMedicalStaffByName(String name) {
        return getAllMedicalStaff().stream()
                .filter(staff -> staff.getMedicalStaffName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public boolean medicalStaffExists(String staffId) {
        return repoMedicalStaff.findByMedicalStaffID(staffId) != null;
    }

    public long getTotalMedicalStaffCount() {
        return getAllMedicalStaff().size();
    }


    public List<MedicalStaff> getNurses() {
        return getMedicalStaffByRole("Nurse");
    }

    public List<MedicalStaff> getTechnicians() {
        return getMedicalStaffByRole("Technician");
    }

    public List<MedicalStaff> getAdministrativeStaff() {
        return getMedicalStaffByRole("Administrative");
    }


    public long getStaffCountByDepartmentAndRole(String departmentId, String role) {
        return getMedicalStaffByDepartment(departmentId).stream()
                .filter(staff -> role.equals(staff.getRole()))
                .count();
    }

    public boolean assignToDepartment(String staffId, String newDepartmentId) {
        MedicalStaff staff = getMedicalStaffById(staffId);
        staff.setDepartamentID(newDepartmentId);
        repoMedicalStaff.save(staff);
        return true;
    }

    public boolean changeRole(String staffId, String newRole) {
        MedicalStaff staff = getMedicalStaffById(staffId);
        staff.setRole(newRole);
        repoMedicalStaff.save(staff);
        return true;
    }


    public boolean isStaffAvailable(String staffId) {
        MedicalStaff staff = getMedicalStaffById(staffId);
        return staff.getAppointments().size() < 10;
    }


    public List<MedicalStaff> findAvailableStaffByDepartmentAndRole(String departmentId, String role) {
        return getMedicalStaffByDepartment(departmentId).stream()
                .filter(staff -> role.equals(staff.getRole()))
                .filter(staff -> isStaffAvailable(staff.getMedicalStaffID()))
                .toList();
    }

}
