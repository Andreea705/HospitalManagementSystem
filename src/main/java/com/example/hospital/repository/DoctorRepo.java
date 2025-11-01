package com.example.hospital.repository;

import com.example.hospital.model.Doctor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;

@Repository
public class DoctorRepo {

    private final List<Doctor> doctors = new ArrayList<>(); //stocheaza toti doctorii

    public Doctor save(Doctor doctor) {
        Doctor existingDoctor = findByLicenseNumber(doctor.getLicenseNumber());

        if (existingDoctor != null) {
            existingDoctor.setName(doctor.getName());
            existingDoctor.setDepartmentID(doctor.getDepartmentID());
            return existingDoctor;

        }
        else{
            doctors.add(doctor);
            return doctor;
        }
    }

    public List<Doctor> findAllDoctors() {
        return new ArrayList<>(this.doctors);
    }

    public Doctor findByLicenseNumber(String licenseNumber) {
        if(licenseNumber == null) return null;

        for(Doctor doctor: doctors){
            if(doctor.getLicenseNumber().equals(licenseNumber)){
                return doctor;
            }
        }
        return null;
    }

    public boolean deleteByLicenseNumber(String licenseNumber) {
        Doctor doctor = findByLicenseNumber(licenseNumber);
        if (doctor != null) {
            doctors.remove(doctor);
            return true;
        }
        return false;
    }

    public boolean existsByLicenseNumber(String licenseNumber) {
        return findByLicenseNumber(licenseNumber) != null;
    }

    public List<Doctor> findByDepartmentID(String departmentID) {
        List<Doctor> result = new ArrayList<>();
        for(Doctor doctor: doctors){
            if(doctor != null && doctor.getDepartmentID().equals(departmentID)){
                result.add(doctor);
            }
        }
        return result;
    }
}
