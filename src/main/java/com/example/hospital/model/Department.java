package com.example.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100, message = "Department name must be 2-100 characters")
    @Column(nullable = false)
    private String name;

    @Min(value = 0, message = "Room numbers cannot be negative")
    @Column(name = "room_numbers")
    private int roomNumbers = 0;

    @Size(max = 100, message = "Department head name cannot exceed 100 characters")
    @Column(name = "department_head")
    private String departmentHead;

    // ============ BEZIEHUNGEN ============

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

//    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Doctor> doctors = new ArrayList<>();

    // ============ KONSTRUKTOREN ============

    public Department() {
        // Default constructor for JPA
    }

    public Department(String name, Hospital hospital) {
        this.name = name;
        this.hospital = hospital;
    }

    public Department(String name, Hospital hospital, int roomNumbers, String departmentHead) {
        this.name = name;
        this.hospital = hospital;
        this.roomNumbers = roomNumbers;
        this.departmentHead = departmentHead;
    }

    // ============ GETTER & SETTER ============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomNumbers() {
        return roomNumbers;
    }

    public void setRoomNumbers(int roomNumbers) {
        this.roomNumbers = roomNumbers;
    }

    public String getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(String departmentHead) {
        this.departmentHead = departmentHead;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

//    public List<Doctor> getDoctors() {
//        return doctors;
//    }
//
//    public void setDoctors(List<Doctor> doctors) {
//        this.doctors = doctors;
//    }

    // Hilfsmethode für hospitalId (falls in Thymeleaf benötigt)
    public Long getHospitalId() {
        return hospital != null ? hospital.getId() : null;
    }

    // ============ HILFSMETHODEN ============

//    public void addDoctor(Doctor doctor) {
//        doctors.add(doctor);
//        doctor.setDepartment(this);
//    }
//
//    public void removeDoctor(Doctor doctor) {
//        doctors.remove(doctor);
//        doctor.setDepartment(null);
//    }

    // ============ BUSINESS LOGIC ============

    public boolean hasCapacity() {
        return this.roomNumbers < 10;
    }

    public boolean isFull() {
        return this.roomNumbers >= 10;
    }

    public int getAvailableCapacity() {
        return Math.max(0, 10 - roomNumbers);
    }

    // ============ toString ============

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roomNumbers=" + roomNumbers +
                ", departmentHead='" + departmentHead + '\'' +
                ", hospital=" + (hospital != null ? hospital.getName() : "null") +
                '}';
    }
}