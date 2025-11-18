package com.example.hospital.repository;

import com.example.hospital.model.Patient;
import org.springframework.stereotype.Repository;

@Repository
public class PatientRepository extends InFileRepository<Patient, String> {

    public PatientRepository() {
        super("patients.json", Patient.class);
    }

    @Override
    protected String getEntityId(Patient patient) {
        return patient.getId();
    }

    @Override
    protected void setEntityId(Patient patient, String id) {
        patient.setId(id);
    }

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected String generateId() {
        return "PAT_" + System.currentTimeMillis();
    }
}

