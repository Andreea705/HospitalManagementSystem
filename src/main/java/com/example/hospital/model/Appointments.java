package com.example.hospital.model;

import java.util.ArrayList;
import java.util.List;

public class Appointments {
    private String appointmentId;
    private String departmentId;
    private String patientId;
    private String admissionDate;
    private AppointmentStatus status; // schimbat din String în enum
    private List<MedicalStaff> medicalStaff;

    public Appointments(String appointmentId, String departmentId, String patientId,
                        String admissionDate, String status) {
        this.appointmentId = appointmentId;
        this.departmentId = departmentId;
        this.patientId = patientId;
        this.admissionDate = admissionDate;
        this.status = status;
        this.medicalStaff = new ArrayList<>();
    }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(String admissionDate) { this.admissionDate = admissionDate; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public List<MedicalStaff> getMedicalStaff() { return medicalStaff; }
    public void setMedicalStaff(List<MedicalStaff> medicalStaff) { this.medicalStaff = medicalStaff; }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId='" + appointmentId + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", admissionDate='" + admissionDate + '\'' +
                ", status=" + status +
                ", medicalStaff=" + medicalStaff +
                '}';
    }
}

