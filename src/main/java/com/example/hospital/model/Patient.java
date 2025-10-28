package com.example.hospital.model;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private String id;
    private String name;
    private int age;
    private String gender;
    private List<Appointment> appointments;
    private String emergencyContact;

    public Patient(String id, String name, int age, String gender, String emergencyContact) {
        this.id = id;
        this.name = name;
        this.age = age;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    public boolean removeAppointment(Appointment appointment) {
        return this.appointments.remove(appointment);
    }
}

