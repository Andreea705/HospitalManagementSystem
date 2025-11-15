
package com.example.hospital.repository;

import com.example.hospital.model.Patient;
import org.springframework.stereotype.Repository;

@Repository
public class PatientRepository extends GenericRepository<Patient, String> {

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


