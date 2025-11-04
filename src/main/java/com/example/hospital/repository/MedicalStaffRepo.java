package com.example.hospital.repository;

import com.example.hospital.model.MedicalStaff;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicalStaffRepo extends GenericRepo<MedicalStaff, String> {

    @Override
    protected String getEntityId(MedicalStaff medicalStaff){
        if(medicalStaff.getMedicalStaffID() == null || medicalStaff.getMedicalStaffID().isEmpty()){
                return "MEDICAL_STAFF" + System.currentTimeMillis();
        }
        return medicalStaff.getMedicalStaffID();
    }

    @Override
    protected String parseId(String id){ return id;}

    public List<MedicalStaff> findByDepartamentID(String departmentID) {
        return storage.values().stream()
                .filter(staff -> staff.getDepartmentID().equals(departmentID))
                .toList();
    }

    public List<MedicalStaff> findByRole(String role) {
        return storage.values().stream()
                .filter(staff -> staff.getRole().equalsIgnoreCase(role))
                .toList();
    }
}
