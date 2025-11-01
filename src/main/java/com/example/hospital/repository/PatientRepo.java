
package com.example.hospital.repository;

import com.example.hospital.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PatientRepo extends GenericRepo<Patient, String> {

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected String getEntityId(Patient patient) {
        if (patient.getId() == null || patient.getId().isEmpty()) {
            return "PAT_" + System.currentTimeMillis();
        }
        return patient.getId();
    }
}


