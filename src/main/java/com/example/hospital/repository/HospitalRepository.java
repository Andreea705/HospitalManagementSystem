package com.example.hospital.repository;

import com.example.hospital.model.Hospital;
import org.springframework.stereotype.Repository;

//O SA VINA IN REPO SAVE IN FILE PT. TEMA VIITOARE
@Repository
public class HospitalRepository extends GenericRepository<Hospital, String> {

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


