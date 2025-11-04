package com.example.hospital.repository;

import com.example.hospital.model.Department;
import com.example.hospital.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Repository
public class DoctorRepo extends GenericRepo<Doctor, String> {

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected String getEntityId(Doctor doctor) {
        // Always generate a new ID if not set
        if (doctor.getMedicalStaffID() == null || doctor.getMedicalStaffID().isEmpty()) {
            String newId = "DOC_" + System.currentTimeMillis();
            doctor.setMedicalStaffID(newId);
            return newId;
        }
        return doctor.getMedicalStaffID();
    }
}
