package com.example.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "medical_staff_appointments")
public class MedicalStaffAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Medical staff ID is required")
    @Column(name = "medical_staff_id", nullable = false)
    private String medicalStaffId;

    @NotBlank(message = "Appointment ID is required")
    @Column(name = "appointment_id", nullable = false)
    private String appointmentId;

    // Constructori
    public MedicalStaffAppointment() {}

    public MedicalStaffAppointment(String medicalStaffId, String appointmentId) {
        this.medicalStaffId = medicalStaffId;
        this.appointmentId = appointmentId;
    }

    // Getters și Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMedicalStaffId() { return medicalStaffId; }
    public void setMedicalStaffId(String medicalStaffId) { this.medicalStaffId = medicalStaffId; }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
}