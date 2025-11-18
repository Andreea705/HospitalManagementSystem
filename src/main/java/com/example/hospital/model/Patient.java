package com.example.hospital.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.Calendar;
import java.util.Objects;

public class Patient {
    private String id;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    private String gender;
    private String emergencyContact;

    public Patient() {
    }

    @JsonCreator
    public Patient(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("dateOfBirth") Date dateOfBirth,
            @JsonProperty("gender") String gender,
            @JsonProperty("emergencyContact") String emergencyContact) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.emergencyContact = emergencyContact != null ? emergencyContact : "";
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    // Age calculation - not stored in JSON
    @JsonIgnore
    public int getAge() {
        if (dateOfBirth == null) return 0;

        Calendar dob = Calendar.getInstance();
        dob.setTime(dateOfBirth);
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    // Format date for display - not stored in JSON
    @JsonIgnore
    public String getFormattedDateOfBirth() {
        if (dateOfBirth == null) return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateOfBirth);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        return String.format("%02d/%02d/%d", day, month, year);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + getFormattedDateOfBirth() +
                ", gender='" + gender + '\'' +
                ", emergencyContact='" + emergencyContact + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}