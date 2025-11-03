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
    protected String parseId(String id) {return id;}

    @Override
    protected String getEntityId(Doctor doctor) {
        if (doctor.getLicenseNumber() == null || doctor.getLicenseNumber().isEmpty()) {
            return "DOC_" + System.currentTimeMillis();
        }
        return doctor.getLicenseNumber();
    }

    public List<Doctor> findByDepartamentId(String departamentId) {
        return storage.values().stream()
                .filter(doc -> doc.getDepartamentID().equals(departamentId))
                .collect(Collectors.toList());
    }

    public List<Doctor> findBySpecialization(String specialization) {
        return storage.values().stream()
                .filter(doc -> doc.getSpecialization().equals(specialization))
                .collect(Collectors.toList());
    }
}
