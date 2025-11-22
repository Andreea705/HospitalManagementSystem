package com.example.hospital.repository;

import com.example.hospital.model.Doctor;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorRepository extends InFileRepository<Doctor, String> {

    public DoctorRepository() {
        super("doctor.json", Doctor.class);
    }

    @Override
    protected String getEntityId(Doctor doctor) {
        if (doctor.getMedicalStaffID() == null || doctor.getMedicalStaffID().isEmpty()) {
            String newId = "DOC_" + System.currentTimeMillis();
            setEntityId(doctor, newId);
            return newId;
        }
        return doctor.getMedicalStaffID();
    }

    @Override
    protected void setEntityId(Doctor doctor, String id) {
        doctor.setMedicalStaffID(id);
    }

    @Override
    protected String parseId(String id) {
        return id;
    }
}
