package com.example.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")

@PrimaryKeyJoinColumn(name = "medical_staff_id")
public class Doctor extends MedicalStaff {

    @NotBlank(message = "Specialization is required")
    @Size(max = 100, message = "Specialization cannot exceed 100 characters")
    @Column(nullable = false)
    private String specialization;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 20, message = "Phone cannot exceed 20 characters")
    private String phone;

    @Size(max = 50, message = "License number cannot exceed 50 characters")
    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    // ============ RELAȚII ============

    // Doctor are o lista de Appointments (OneToMany)
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointments> doctorAppointments = new ArrayList<>();

    // ============ CONSTRUCTORI ============

    public Doctor() {
        super();
        this.setRole("doctor");
    }

    public Doctor(String medicalStaffId, String medicalStaffName, String specialization) {
        super(medicalStaffId, medicalStaffName, "doctor", null);
        this.specialization = specialization;
    }

    public Doctor(String medicalStaffId, String medicalStaffName, String specialization, Department department) {
        super(medicalStaffId, medicalStaffName, "doctor", department);
        this.specialization = specialization;
    }

    // ============ GETTERI & SETTERI ============

    // ID-ul este moștenit din MedicalStaff

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public List<Appointments> getDoctorAppointments() {
        return doctorAppointments;
    }

    public void setDoctorAppointments(List<Appointments> doctorAppointments) {
        this.doctorAppointments = doctorAppointments;
    }

    // Suprascriere metode pentru compatibilitate
    @Override
    public List<Appointments> getAppointments() {
        return doctorAppointments;
    }

    @Override
    public void setAppointments(List<Appointments> appointments) {
        this.doctorAppointments = appointments;
    }

    // ============ HELPER METODE ============

    @Transient
    public String getDepartmentName() {
        return this.getDepartment() != null ? this.getDepartment().getName() : "Not assigned";
    }

    @Transient
    public String getHospitalName() {
        return this.getDepartment() != null && this.getDepartment().getHospital() != null
                ? this.getDepartment().getHospital().getName() : "Unknown";
    }

    @Transient
    public Long getHospitalId() {
        return this.getDepartment() != null && this.getDepartment().getHospital() != null
                ? this.getDepartment().getHospital().getId() : null;
    }

    @Transient
    public int getAppointmentCount() {
        return this.doctorAppointments != null ? this.doctorAppointments.size() : 0;
    }

    @Transient
    public int getActiveAppointmentCount() {
        if (this.doctorAppointments == null) return 0;
        return (int) this.doctorAppointments.stream()
                .filter(appointment -> appointment.getStatus() == AppointmentStatus.ACTIVE)
                .count();
    }

    @Transient
    public int getCompletedAppointmentCount() {
        if (this.doctorAppointments == null) return 0;
        return (int) this.doctorAppointments.stream()
                .filter(appointment -> appointment.getStatus() == AppointmentStatus.COMPLETED)
                .count();
    }

    // Business logic methods
    public boolean hasAppointments() {
        return this.doctorAppointments != null && !this.doctorAppointments.isEmpty();
    }

    public boolean isAssignedToDepartment() {
        return this.getDepartment() != null;
    }

    public void addAppointment(Appointments appointment) {
        if (this.doctorAppointments == null) {
            this.doctorAppointments = new ArrayList<>();
        }
        this.doctorAppointments.add(appointment);
        appointment.setDoctor(this);
    }

    public void removeAppointment(Appointments appointment) {
        if (this.doctorAppointments != null) {
            this.doctorAppointments.remove(appointment);
            appointment.setDoctor(null);
        }
    }

    // ============ toString ============

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + this.getId() +
                ", name='" + this.getMedicalStaffName() + '\'' +
                ", medicalStaffID='" + this.getMedicalStaffId() + '\'' +
                ", specialization='" + specialization + '\'' +
                ", department=" + (this.getDepartment() != null ? this.getDepartment().getName() : "null") +
                '}';
    }
}
