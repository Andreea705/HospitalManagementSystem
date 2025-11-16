package com.example.hospital.repository;

import com.example.hospital.model.Hospital;
import org.springframework.stereotype.Repository;

@Repository
public class HospitalRepository extends InFileRepository<Hospital, String> {

    public HospitalRepository() {
        super("hospitals.json", Hospital.class);
    }

    @Override
    protected String getEntityId(Hospital hospital) {
        return hospital.getId();
    }

    @Override
    protected void setEntityId(Hospital hospital, String id) {
        hospital.setId(id);
    }

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected String generateId() {

        return "HOSP_" + System.currentTimeMillis();
    }
}
