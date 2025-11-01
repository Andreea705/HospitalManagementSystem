package com.example.hospital.repository;

import com.example.hospital.model.Hospital;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HospitalRepo extends GenericRepo<Hospital, String> {

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected String getEntityId(Hospital hospital) {
        if (hospital.getId() == null || hospital.getId().isEmpty()) {
            return "HOSP_" + System.currentTimeMillis();
        }
        return hospital.getId();
    }

}


