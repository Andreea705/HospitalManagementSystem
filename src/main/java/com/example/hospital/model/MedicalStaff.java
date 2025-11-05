package com.example.hospital.model;

import java.util.ArrayList;
import java.util.List;


public abstract class MedicalStaff {

    private String id;
    private String medicalStaffID;
    private String medicalStaffName;
    private List<Appointments> appointments;
    private String departmentID;
    private String role; //nurse, technician

    public MedicalStaff() {
        this.appointments = new ArrayList<>();
    }

    public MedicalStaff(String medicalStaffID, String medicalStaffName, String departmentID, String role) {
        this.medicalStaffID = medicalStaffID;
        this.medicalStaffName = medicalStaffName;
        this.departmentID = departmentID;
        this.appointments = new ArrayList<>();
        this.role = role;
    }


    public String getDepartmentID() { // ← FIXED: removed extra 'a'
        return departmentID;
    }

    public void setDepartmentID(String departmentID) { // ← FIXED: removed extra 'a'
        this.departmentID = departmentID;
    }

    // ... rest of your getters/setters stay the same
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicalStaffID() {
        return medicalStaffID;
    }

    public void setMedicalStaffID(String medicalStaffID) {
        this.medicalStaffID = medicalStaffID;
    }

    public String getMedicalStaffName() {
        return medicalStaffName;
    }

    public void setMedicalStaffName(String medicalStaffName) {
        this.medicalStaffName = medicalStaffName;
    }

    public List<Appointments> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointments> appointments) {
        this.appointments = appointments;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}