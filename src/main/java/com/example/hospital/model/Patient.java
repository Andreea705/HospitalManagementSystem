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
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    // FIXED: Proper relationship with Appointments
    @OneToMany(mappedBy = "patient", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private List<Appointments> appointments = new ArrayList<>();

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

    @Column(nullable = false, updatable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();

    // ============ CONSTRUCTORS ============
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
                   LocalDate dateOfBirth, String emergencyContact) {
        this.patientId = patientId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.emergencyContact = emergencyContact;
        this.registrationDate = LocalDateTime.now();
    }

    // ============ GETTERS & SETTERS ============
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

    // FIXED: Added getter for appointments
    public List<Appointments> getAppointments() {
        if (appointments == null) {
            appointments = new ArrayList<>();
        }
        return appointments;
    }

    // FIXED: Added setter for appointments
    public void setAppointments(List<Appointments> appointments) {
        this.appointments = appointments;
        // Ensure bidirectional relationship
        if (appointments != null) {
            for (Appointments appointment : appointments) {
                if (appointment.getPatient() != this) {
                    appointment.setPatient(this);
                }
            }
        }
    }

    // Helper method to add appointment
    public void addAppointment(Appointments appointment) {
        if (appointments == null) {
            appointments = new ArrayList<>();
        }
        appointments.add(appointment);
        appointment.setPatient(this);
    }

    // Helper method to remove appointment
    public void removeAppointment(Appointments appointment) {
        if (appointments != null) {
            appointments.remove(appointment);
            appointment.setPatient(null);
        }
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

    // ============ HELPER METHODS ============
    @Transient
    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    @Transient
    public int getAppointmentsCount() {
        return appointments != null ? appointments.size() : 0;
    }

    @Transient
    public List<Appointments> getActiveAppointments() {
        if (this.getAppointments() == null) return new ArrayList<>();
        return this.getAppointments().stream()
                .filter(a -> "ACTIVE".equals(a.getStatus()))
                .collect(Collectors.toList());
    }

    @Transient
    public List<Appointments> getCompletedAppointments() {
        if (this.getAppointments() == null) return new ArrayList<>();
        return this.getAppointments().stream()
                .filter(a -> "COMPLETED".equals(a.getStatus()))
                .collect(Collectors.toList());
    }

    @Transient
    public List<Appointments> getUpcomingAppointments() {
        if (this.getAppointments() == null) return new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        return this.getAppointments().stream()
                .filter(a -> "ACTIVE".equals(a.getStatus()))
                .filter(a -> a.getAppointmentDate() != null && a.getAppointmentDate().isAfter(now))
                .collect(Collectors.toList());
    }

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
                ", appointmentsCount=" + getAppointmentsCount() +
                ", registrationDate=" + registrationDate +
                '}';
    }

    // ============ equals & hashCode ============
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return id != null && id.equals(patient.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}