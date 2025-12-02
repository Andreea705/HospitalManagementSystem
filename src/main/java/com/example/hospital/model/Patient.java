package com.example.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Patient ID is required")
    @Size(min = 3, max = 20, message = "Patient ID must be between 3 and 20 characters")
    @Column(nullable = false, unique = true)
    private String patientId;


    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

//    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Appointments> appointments = new ArrayList<>();


    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;


    @Pattern(regexp = "^\\+?[0-9\\-\\s]{10,}$", message = "Phone number must be valid")
    @Column(nullable = false)
    private String phoneNumber;


    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column
    private String emergencyContact;

    @Column(nullable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();


    public Patient() {
    }


    public Patient(String patientId, String name, String email, String phoneNumber,
                   LocalDate dateOfBirth) {
        this.patientId = patientId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.registrationDate = LocalDateTime.now();
    }


    public Patient(String patientId, String name, String email, String phoneNumber,
                   LocalDate dateOfBirth, String address, String bloodType,
                   String emergencyContact) {
        this.patientId = patientId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.emergencyContact = emergencyContact;
        this.registrationDate = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

//    public List<Appointments> getAppointments() {
//        return appointments;
//    }
//
//    public void setAppointments(List<Appointments> appointments) {
//        this.appointments = appointments;
//    }

    // ============ Helper Methods ============
//
//    public void addAppointment(Appointments appointment) {
//        if (appointments == null) {
//            appointments = new ArrayList<>();
//        }
//        appointments.add(appointment);
//        appointment.setPatient(this);
//    }
//
//    public void removeAppointment(Appointments appointment) {
//        if (appointments != null) {
//            appointments.remove(appointment);
//            appointment.setPatient(null);
//        }
//    }

    public int getAge() {
        if (dateOfBirth == null) return 0;
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

//    public boolean hasActiveAppointments() {
//        if (appointments == null || appointments.isEmpty()) {
//            return false;
//        }
//        // Assuming Appointment has a status field
//        return appointments.stream()
//                .anyMatch(app -> "Active".equals(app.getStatus()) ||
//                        "Scheduled".equals(app.getStatus()));
//    }

    // ============ toString ============

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", patientId='" + patientId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", emergencyContact='" + emergencyContact + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}

//, appointmentsCount=" + (appointments != null ? appointments.size() : 0) +