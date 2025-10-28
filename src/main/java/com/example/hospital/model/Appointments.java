package com.example.hospital.model;

import java.util.ArrayList;
import java.util.List;

public class Appointments {
    private String appointmentId;
    private String departmentId;
    private String patientId;
    private String admissionDate;
    private String status;
    private List<MedicalStaff> medicalStaff;

    public Appointments(String appointmentId, String departmentId, String patientId, String admissionDate, String status) {
        this.appointmentId = appointmentId;
        this.departmentId = departmentId;
        this.patientId = patientId;
        this.admissionDate = admissionDate;
        this.status = status;
        this.medicalStaff = new ArrayList<>();
    }

    public String getAppointmentId() {return this.appointmentId;}

    public void setAppointmentId(String appointmentId) {this.appointmentId = appointmentId;}

    public String getDepartmentId() {return this.departmentId;}

    public void setDepartmentId(String departmentId) {this.departmentId = departmentId;}

    public String getPatientId() {return this.patientId;}

    public void setPatientId(String patientId) {this.patientId = patientId;}

    public String getAdmissionDate() {return this.admissionDate;}

    public void setAdmissionDate(String admissionDate) {this.admissionDate = admissionDate;}

    public String getStatus() {return this.status;}
    public void setStatus(String status) {this.status = status;}

    public List<MedicalStaff> getMedicalStaff() {return this.medicalStaff;}

    public void setMedicalStaff(List<MedicalStaff> medicalStaff) {this.medicalStaff = medicalStaff;}

}
