package com.example.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Room number is required")
    @Size(max = 20, message = "Room number cannot exceed 20 characters")
    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @NotBlank(message = "Room type is required")
    @Size(max = 50, message = "Room type cannot exceed 50 characters")
    @Column(nullable = false)
    private String type;

    @Min(value = 1, message = "Capacity must be at least 1")
    @Column(nullable = false)
    private int capacity;

    @NotNull(message = "Availability status is required")
    @Column(nullable = false)
    private boolean available = true;

    // ============ BEZIEHUNGEN ============

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

//    @OneToMany(mappedBy = "room")
//    private List<Appointments> appointments = new ArrayList<>();

    // ============ KONSTRUKTOREN ============

    public Room() {
        // Default constructor for JPA
    }

    public Room(String roomNumber, String type, int capacity, Hospital hospital) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.capacity = capacity;
        this.hospital = hospital;
        this.available = true;
    }

    public Room(String roomNumber, String type, int capacity, boolean available, Hospital hospital) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.capacity = capacity;
        this.available = available;
        this.hospital = hospital;
    }

    // ============ GETTER & SETTER ============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

//    public List<Appointments> getAppointments() {
//        return appointments;
//    }
//
//    public void setAppointments(List<Appointments> appointments) {
//        this.appointments = appointments;
//    }

    // Hilfsmethode für hospitalId
    public Long getHospitalId() {
        return hospital != null ? hospital.getId() : null;
    }

    // ============ BUSINESS LOGIC ============

    public boolean canAccommodate(int patients) {
        return patients <= capacity && available;
    }

    public void occupy() {
        this.available = false;
    }

    public void vacate() {
        this.available = true;
    }

    public String getStatus() {
        return available ? "Available" : "Occupied";
    }

    public void setStatus(String status) {
        this.available = "Available".equalsIgnoreCase(status);
    }

    // ============ toString ============

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                ", available=" + available +
                ", hospital=" + (hospital != null ? hospital.getName() : "null") +
                '}';
    }
}