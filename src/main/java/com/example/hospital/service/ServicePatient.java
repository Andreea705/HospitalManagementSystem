package com.example.hospital.service;

import com.example.hospital.model.Patient;
import com.example.hospital.repository.RepoPatient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePatient {
    private final RepoPatient repoPatient;

    @Autowired
    public ServicePatient(RepoPatient repoPatient) {
        this.repoPatient = repoPatient;
    }

    public Patient createPatient(Patient patient) {
        validatePatient(patient);
        return repoPatient.save(patient);
    }

    public List<Patient> getAllPatients() {
        return repoPatient.findAll();
    }

    public Patient getPatientById(String id) {
        Patient patient = repoPatient.findById(id);
        if (patient == null) {
            throw new RuntimeException("Patient not found with id: " + id);
        }
        return patient;
    }

    public Patient updatePatient(String id, Patient updatedPatient) {
        Patient existingPatient = getPatientById(id);

        existingPatient.setName(updatedPatient.getName());
        existingPatient.setAge(updatedPatient.getAge());
        existingPatient.setAge(updatedPatient.getAge());
        existingPatient.setEmergencyContact(updatedPatient.getEmergencyContact());
        existingPatient.setAppointments(updatedPatient.getAppointments());

        validatePatient(existingPatient);
        return repoPatient.save(existingPatient);
    }

    public boolean deletePatient(String id) {
        if (repoPatient.findById(id) != null) {
            return repoPatient.deleteById(id);
        }
        return false;
    }

    private void validatePatient(Patient patient) {
        if (patient.getName() == null || patient.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name cannot be empty");
        }
        if (patient.getId() == null || patient.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient ID cannot be empty");
        }
        if (patient.getAge() < 0 || patient.getAge() > 150) {
            throw new IllegalArgumentException("Patient age must be between 0 and 150");
        }
        if (patient.getEmergencyContact() == null || patient.getEmergencyContact().trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact cannot be empty");
        }
    }

    public List<Patient> findPatientsByName(String name) {
        return getAllPatients().stream()
                .filter(patient -> patient.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }


    public List<Patient> findPatientsByAgeRange(int minAge, int maxAge) {
        return getAllPatients().stream()
                .filter(patient -> patient.getAge() >= minAge && patient.getAge() <= maxAge)
                .toList();
    }

    public boolean patientExists(String id) {
        return repoPatient.findById(id) != null;
    }

    public long getTotalPatientCount() {
        return getAllPatients().size();
    }

    public boolean canScheduleAppointment(String patientId) {
        Patient patient = getPatientById(patientId);

        long activeAppointments = patient.getAppointments().stream()
                .filter(appt -> "Active".equals(appt.getStatus()))
                .count();
        return activeAppointments < 5;
    }

    public List<Patient> findPatientsByEmergencyContact(String contact) {
        return getAllPatients().stream()
                .filter(patient -> contact.equals(patient.getEmergencyContact()))
                .toList();
    }

    public PatientStatistics getPatientStatistics() {
        List<Patient> allPatients = getAllPatients();

        long adultPatients = allPatients.stream().filter(p -> p.getAge() >= 18).count();
        long pediatricPatients = allPatients.stream().filter(p -> p.getAge() < 18).count();

        return new PatientStatistics(
                allPatients.size(),
                adultPatients,
                pediatricPatients
        );
    }

    public static class PatientStatistics {
        private final long totalPatients;
        private final long adultPatients;
        private final long pediatricPatients;

        public PatientStatistics(long totalPatients, long adultPatients, long pediatricPatients) {
            this.totalPatients = totalPatients;
            this.adultPatients = adultPatients;
            this.pediatricPatients = pediatricPatients;
        }


        public long getTotalPatients() { return totalPatients; }
        public long getAdultPatients() { return adultPatients; }
        public long getPediatricPatients() { return pediatricPatients; }
    }
}