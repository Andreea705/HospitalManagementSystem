
package com.example.hospital.repository;

import com.example.hospital.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RepoPatient {
    private final List<Patient> patients = new ArrayList<>();

    public Patient save(Patient patient) {
        Patient existingPatient = findById(patient.getId());

        if (existingPatient != null) {

            existingPatient.setName(patient.getName());
            existingPatient.setAge(patient.getAge());
            existingPatient.setEmergencyContact(patient.getEmergencyContact());
            existingPatient.setAppointments(patient.getAppointments());

            return existingPatient;
        }
        else {

            patients.add(patient);
            return patient;
        }
    }

    public List<Patient> findAll() {
        return new ArrayList<>(patients);
    }

    public Patient findById(String id) {
        if (id == null) return null;

        for (Patient patient : patients) {
            if (patient != null && id.equals(patient.getId())) {
                return patient;
            }
        }

        return null;
    }

    public boolean deleteById(String id) {
        Patient patient = findById(id);
        if (patient != null) {
            return patients.remove(patient);
        }

        return false;
    }

    public boolean existsById(String id) {
        return findById(id) != null;
    }

    public long count() {
        return patients.size();
    }


    public List<Patient> findByName(String name) {
        List<Patient> result = new ArrayList<>();

        for (Patient patient : patients) {
            if (patient != null && name.equalsIgnoreCase(patient.getName())) {
                result.add(patient);
            }
        }

        return result;
    }


}

