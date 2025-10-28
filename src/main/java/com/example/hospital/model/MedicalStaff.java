package com.example.hospital.model;

import java.util.ArrayList;
import java.util.List;

public class MedicalStaff {
    private String medicalStaffID;
    private String medicalStaffName;
    private List<Appointments> appointments;
    private String departamentID;
    private String role; //nurse, tehnician

    public MedicalStaff(String medicalStaffID, String medicalStaffName,  String departamentID, String role) {
        this.medicalStaffID = medicalStaffID;
        this.medicalStaffName = medicalStaffName;
        this.departamentID = departamentID;
        this.appointments = new ArrayList<>();
        this.role = role;
    }

    public String getMedicalStaffID() {return medicalStaffID;}

    public void setMedicalStaffID(String medicalStaffID) {this.medicalStaffID = medicalStaffID;}

    public String getMedicalStaffName() {return medicalStaffName;}

    public void setMedicalStaffName(String medicalStaffName) {this.medicalStaffName = medicalStaffName;}

    public String getDepartamentID() {return departamentID;}

    public void setDepartamentID(String departamentID) {this.departamentID = departamentID;}

    public List<Appointments> getAppointments() {return appointments;}

    public void setAppointments(List<Appointments> appointments) {this.appointments = appointments;}

    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}
}
