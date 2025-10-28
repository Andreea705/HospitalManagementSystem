package com.example.hospital.model;

public class MedicalStaffAppointment {
    private String medicalStaffAppointmentId;
    private String medicalStaffId;
    private String appointmentID;

    public MedicalStaffAppointment(String medicalStaffAppointmentId, String medicalStaffId, String appointmentID) {
        this.medicalStaffAppointmentId = medicalStaffAppointmentId;
        this.medicalStaffId = medicalStaffId;
        this.appointmentID = appointmentID;

    }

    public String getMedicalStaffAppointmentId() {return medicalStaffAppointmentId;}

    public void setMedicalStaffAppointmentId(String medicalStaffAppointmentId) {this.medicalStaffAppointmentId = medicalStaffAppointmentId;}

    public String getMedicalStaffId() {return medicalStaffId;}

    public void setMedicalStaffId(String medicalStaffId) {this.medicalStaffId = medicalStaffId;}

    public String getAppointmentID() {return appointmentID;}

    public void setAppointmentID(String appointmentID) {this.appointmentID = appointmentID;}
}
