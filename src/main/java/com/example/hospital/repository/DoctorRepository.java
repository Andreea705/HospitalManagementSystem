package com.example.hospital.repository;

import com.example.hospital.model.Doctor;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorRepository extends GenericRepository<Doctor, String> {

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected String getEntityId(Doctor doctor) {

        if (doctor.getMedicalStaffID() == null || doctor.getMedicalStaffID().isEmpty()) {
            String newId = "DOC_" + System.currentTimeMillis();
            doctor.setMedicalStaffID(newId);
            return newId;
        }
        return doctor.getMedicalStaffID();
    }
}
