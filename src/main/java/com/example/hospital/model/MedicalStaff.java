package com.example.hospital.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "role_type")
@Table(name = "medical_staff")
public abstract class MedicalStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medical_staff_name", nullable = false)
    private String medicalStaffName;

    @Column(name = "medical_staff_id", unique = true, nullable = false)
    private String medicalStaffId;

    @Column(name = "role", nullable = false)
    private String role;

    // ============ RELAȚII ============

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Transient
    private List<Appointments> appointments = new ArrayList<>();

//    @OneToMany(mappedBy = "medicalStaff", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<MedicalStaffAppointment> medical_staffappointments = new ArrayList<>();

    // ============ CONSTRUCTORI ============

    public MedicalStaff() {
    }

    public MedicalStaff(String medicalStaffId, String medicalStaffName, String role, Department department) {
        this.medicalStaffId = medicalStaffId;
        this.medicalStaffName = medicalStaffName;
        this.role = role;
        this.department = department;
    }

    // ============ GETTERI & SETTERI ============

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMedicalStaffName() { return medicalStaffName; }
    public void setMedicalStaffName(String medicalStaffName) { this.medicalStaffName = medicalStaffName; }

    public String getMedicalStaffId() { return medicalStaffId; }
    public void setMedicalStaffId(String medicalStaffId) { this.medicalStaffId = medicalStaffId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public List<Appointments> getAppointments() { return appointments; }
    public void setAppointments(List<Appointments> appointments) { this.appointments = appointments; }

//    public void setMedical_StaffAppointments(List<MedicalStaffAppointment> medical_staffappointments) {
//        this.medical_staffappointments = medical_staffappointments;
//    }

    // ============ HELPER METHODS ============

    @Transient
    public String getName() {
        return medicalStaffName;
    }

    @Transient
    public Long getDepartmentId() {
        return department != null ? department.getId() : null;
    }

}