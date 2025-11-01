package com.example.hospital.repository;

import com.example.hospital.model.Hospital;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HospitalRepo {
    private final List<Hospital> hospitals = new ArrayList<>();

    public Hospital save(Hospital hospital) {
        Hospital existingHospital = findById(hospital.getId());

        if (existingHospital != null) {
            existingHospital.setName(hospital.getName());
            existingHospital.setCity(hospital.getCity());
            existingHospital.setDepartments(hospital.getDepartments());
            existingHospital.setRooms(hospital.getRooms());
            return existingHospital;
        }
        else {
            hospitals.add(hospital);
            return hospital;
        }
    }

    public List<Hospital> findAll() {
        return new ArrayList<>(hospitals);
    }

    public Hospital findById(String id) {
        if (id == null) {
            return null;
        }

        for (Hospital hospital : hospitals) {
            if (hospital != null && id.equals(hospital.getId())) {
                return hospital;
            }
        }
        return null;
    }

    public boolean deleteById(String id) {
        Hospital hospital = findById(id);
        if (hospital != null) {
            return hospitals.remove(hospital);
        }
        return false;
    }


}

