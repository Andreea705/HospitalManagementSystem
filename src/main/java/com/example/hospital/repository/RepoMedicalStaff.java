package com.example.hospital.repository;

import com.example.hospital.model.MedicalStaff;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RepoMedicalStaff {

    private final List<MedicalStaff> medicalStaffList = new ArrayList<>();

    public MedicalStaff save(MedicalStaff staff) {
        MedicalStaff existingStaff = findByMedicalStaffID(staff.getMedicalStaffID());

        if (existingStaff != null) {
            // Dacă există deja, actualizăm atributele
            existingStaff.setMedicalStaffName(staff.getMedicalStaffName());
            existingStaff.setDepartamentID(staff.getDepartamentID());
            existingStaff.setRole(staff.getRole());
            existingStaff.setAppointments(staff.getAppointments());
            return existingStaff;
        } else {
            // Dacă e nou, îl adăugăm în listă
            medicalStaffList.add(staff);
            return staff;
        }
    }

    public List<MedicalStaff> findAll(){
        return new ArrayList<>(medicalStaffList);
    }

    public MedicalStaff findByMedicalStaffID(String medicalStaffID){
        if(medicalStaffID == null) return null;

        for(MedicalStaff staff: medicalStaffList){
            if(staff.getMedicalStaffID().equals(medicalStaffID)){
                return staff;
            }
        }
        return null;
    }

    public boolean deleteByMedicalStaffID(String medicalStaffID){
        MedicalStaff staff = findByMedicalStaffID(medicalStaffID);
        if(staff != null){
            medicalStaffList.remove(staff);
            return true;
        }
        return false;
    }

    public boolean existsByMedicalStaffID(String medicalStaffID){
        return findByMedicalStaffID(medicalStaffID) != null;
    }

    public List<MedicalStaff> findByDepartamentID(String  departamentID) {
        List<MedicalStaff> result = new ArrayList<>();
        for (MedicalStaff staff : medicalStaffList) {
            if (staff != null && staff.getDepartamentID().equals(departamentID))
                result.add(staff);
        }
        return result;
    }

    public List<MedicalStaff> findByRole(String  role) {
        List<MedicalStaff> result = new ArrayList<>();
        for (MedicalStaff staff : medicalStaffList) {
            if (staff != null && staff.getRole().equals(role))
                result.add(staff);
        }
        return result;
    }
}
