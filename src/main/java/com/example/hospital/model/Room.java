package com.example.hospital.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String id;
    private String hospitalId;
    private Double capacity;
    private String number;
    private String status;
    private List<Appointments> appointments;

    public Room(String id, String hospitalId, Double capacity, String type, String status) {
        this.id = id;
        this.hospitalId = hospitalId;
        this.capacity = capacity;
        this.number = number;
        this.status = status;
        this.appointments = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Appointments> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointments> appointments) {
        this.appointments = appointments;
    }

    public void addAppointment(Appointments appointment) {
        this.appointments.add(appointment);
    }

    public boolean removeAppointment(Appointments appointment) {
        return this.appointments.remove(appointment);
    }
}