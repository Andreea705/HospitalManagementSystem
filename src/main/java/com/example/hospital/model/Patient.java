package com.example.hospital.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient {
    private String id;
    private String name;
    private Date dateOfBirth;
    private String gender;
    private List<Appointments> appointments;
    private String emergencyContact;

    public Patient(String id, String name, Date dateOfBirth, String gender, String emergencyContact) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.appointments = new ArrayList<>();
        this.emergencyContact = emergencyContact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getAge() { return dateOfBirth;
    }

    public void setAge(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Appointments> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointments> appointments) {
        this.appointments = appointments;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public void addAppointment(Appointments appointment) {
        this.appointments.add(appointment);
    }

    public boolean removeAppointment(Appointments appointment) {
        return this.appointments.remove(appointment);
    }
}

