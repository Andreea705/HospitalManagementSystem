package com.example.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "nurses")
@PrimaryKeyJoinColumn(name = "medical_staff_id")
public class Nurse extends MedicalStaff {

    @NotNull(message = "Qualification level is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "qualification_level", nullable = false)
    private QualificationLevel qualificationLevel;

    @NotBlank(message = "Shift is required")
    @Size(max = 20, message = "Shift cannot exceed 20 characters")
    @Column(nullable = false)
    private String shift;

    @Column(name = "on_duty")
    private boolean onDuty = true;

    // ============ CONSTRUCTORI ============

    public Nurse() {
        super();
        this.setRole("nurse");
    }

    public Nurse(String medicalStaffId, String medicalStaffName,
                 QualificationLevel qualificationLevel, String shift) {
        super(medicalStaffId, medicalStaffName, "nurse", null);
        this.qualificationLevel = qualificationLevel;
        this.shift = shift;
        this.onDuty = true;
    }

    public Nurse(String medicalStaffId, String medicalStaffName,
                 QualificationLevel qualificationLevel, String shift,
                 Department department) {
        super(medicalStaffId, medicalStaffName, "nurse", department);
        this.qualificationLevel = qualificationLevel;
        this.shift = shift;
        this.onDuty = true;
    }

    // ============ GETTERI & SETTERI ============

    public QualificationLevel getQualificationLevel() {
        return qualificationLevel;
    }

    public void setQualificationLevel(QualificationLevel qualificationLevel) {
        this.qualificationLevel = qualificationLevel;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public boolean isOnDuty() {
        return onDuty;
    }

    public void setOnDuty(boolean onDuty) {
        this.onDuty = onDuty;
    }

    // ============ HELPER METODE ============

    @Transient
    public String getDepartmentName() {
        return this.getDepartment() != null ? this.getDepartment().getName() : "Not assigned";
    }

    @Transient
    public int getAppointmentCount() {
        return 0;
    }

    @Transient
    public int getActiveAppointmentCount() {
        return 0;
    }

    public boolean hasAppointments() {
        return false;
    }

    public boolean isAssignedToDepartment() {
        return this.getDepartment() != null;
    }

    public void toggleDuty() {
        this.onDuty = !this.onDuty;
    }

    public void promote(QualificationLevel newLevel) {
        if (newLevel != null) {
            this.qualificationLevel = newLevel;
        }
    }

    @Override
    public String toString() {
        return "Nurse{" +
                "id=" + this.getId() +
                ", name='" + this.getMedicalStaffName() + '\'' +
                ", medicalStaffID='" + this.getMedicalStaffId() + '\'' +
                ", qualificationLevel=" + qualificationLevel +
                ", shift='" + shift + '\'' +
                ", onDuty=" + onDuty +
                ", department=" + (this.getDepartment() != null ? this.getDepartment().getName() : "null") +
                '}';
    }
}
