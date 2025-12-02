package com.example.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @NotBlank(message = "Patient name is required")
    @Size(max = 100, message = "Patient name cannot exceed 100 characters")
    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String description;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.ACTIVE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // ============ RELAȚII ============

    @NotNull(message = "Department is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    // ============ CONSTRUCTORI ============

    public Appointments() {
        // Default constructor for JPA
    }

    public Appointments(LocalDateTime appointmentDate, String patientName, Department department) {
        this.appointmentDate = appointmentDate;
        this.patientName = patientName;
        this.department = department;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ============ GETTERI & SETTERI ============

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) {
        this.department = department;
    }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    // ============ HELPER METODE ============

    @Transient
    public Long getDepartmentId() {
        return department != null ? department.getId() : null;
    }

    @Transient
    public Long getDoctorId() {
        return doctor != null ? doctor.getId() : null;
    }

    @Transient
    public String getDoctorName() {
        if (doctor == null) {
            return "Not assigned";
        }
        return doctor.getMedicalStaffName();
    }

    @Transient
    public String getDepartmentName() {
        return department != null ? department.getName() : "Unknown";
    }

    // Business logic methods
    public boolean isCompleted() {
        return status == AppointmentStatus.COMPLETED;
    }

    public boolean isActive() {
        return status == AppointmentStatus.ACTIVE;
    }

    public boolean isPast() {
        return appointmentDate != null && appointmentDate.isBefore(LocalDateTime.now());
    }

    // ============ toString ============

    @Override
    public String toString() {
        return "Appointments{" +
                "id=" + id +
                ", appointmentDate=" + appointmentDate +
                ", patientName='" + patientName + '\'' +
                ", department=" + (department != null ? department.getName() : "null") +
                ", status=" + status +
                '}';
    }
}