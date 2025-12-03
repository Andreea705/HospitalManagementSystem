package com.example.hospital.service;

import com.example.hospital.model.Appointments;
import com.example.hospital.model.Department;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.Patient;
import com.example.hospital.model.AppointmentStatus;
import com.example.hospital.repository.AppointmentsRepository;
import com.example.hospital.repository.DepartmentRepository;
import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AppointmentsService {

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    // ============ CRUD OPERATIONS ============

    public List<Appointments> getAllAppointments() {
        return appointmentsRepository.findAll();
    }

    public Appointments getAppointmentById(Long id) {
        return appointmentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    }

    // UPDATED: Add patientId parameter
    public Appointments createAppointment(Appointments appointment, Long departmentId, Long patientId) {
        validateAppointmentData(appointment);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        appointment.setDepartment(department);
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.ACTIVE);
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());

        // Sync patient name
        appointment.setPatientName(patient.getName());

        return appointmentsRepository.save(appointment);
    }

    // UPDATED: Add patientId parameter
    public Appointments createAppointmentWithDoctor(Appointments appointment, Long departmentId,
                                                    Long doctorId, Long patientId) {
        validateAppointmentData(appointment);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        // Check if doctor belongs to the same department
        if (!doctor.getDepartment().getId().equals(departmentId)) {
            throw new RuntimeException("Doctor does not belong to the selected department");
        }

        appointment.setDepartment(department);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.ACTIVE);
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());

        // Sync patient name
        appointment.setPatientName(patient.getName());

        return appointmentsRepository.save(appointment);
    }

    public Appointments updateAppointment(Long id, Appointments appointmentDetails) {
        Appointments appointment = getAppointmentById(id);

        validateAppointmentData(appointmentDetails);

        appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
        appointment.setPatientName(appointmentDetails.getPatientName());
        appointment.setDescription(appointmentDetails.getDescription());
        appointment.setStatus(appointmentDetails.getStatus());
        appointment.setUpdatedAt(LocalDateTime.now());

        // Update patient if provided
        if (appointmentDetails.getPatient() != null) {
            appointment.setPatient(appointmentDetails.getPatient());
        }

        return appointmentsRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        Appointments appointment = getAppointmentById(id);
        appointmentsRepository.delete(appointment);
    }

    public List<Appointments> getAppointmentsByPatientId(Long patientId) {
        return appointmentsRepository.findByPatientId(patientId);
    }

    // ============ BUSINESS OPERATIONS ============

    public Appointments completeAppointment(Long id) {
        Appointments appointment = getAppointmentById(id);

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new RuntimeException("Appointment is already completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setUpdatedAt(LocalDateTime.now());

        return appointmentsRepository.save(appointment);
    }

    public Appointments activateAppointment(Long id) {
        Appointments appointment = getAppointmentById(id);

        if (appointment.getStatus() == AppointmentStatus.ACTIVE) {
            throw new RuntimeException("Appointment is already active");
        }

        appointment.setStatus(AppointmentStatus.ACTIVE);
        appointment.setUpdatedAt(LocalDateTime.now());

        return appointmentsRepository.save(appointment);
    }

    public Appointments assignDoctor(Long appointmentId, Long doctorId) {
        Appointments appointment = getAppointmentById(appointmentId);

        if (doctorId == null) {
            appointment.setDoctor(null);
        } else {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

            // Check if doctor belongs to the appointment's department
            if (!doctor.getDepartment().getId().equals(appointment.getDepartment().getId())) {
                throw new RuntimeException("Doctor does not belong to the appointment's department");
            }

            appointment.setDoctor(doctor);
        }

        appointment.setUpdatedAt(LocalDateTime.now());

        return appointmentsRepository.save(appointment);
    }

    // ============ SEARCH AND FILTER METHODS ============

    public List<Appointments> searchAppointments(String patientName, Long departmentId,
                                                 AppointmentStatus status,
                                                 LocalDateTime startDate, LocalDateTime endDate) {
        if (patientName != null && !patientName.trim().isEmpty()) {
            return appointmentsRepository.findByPatientNameContainingIgnoreCase(patientName);
        }

        if (departmentId != null) {
            if (status != null) {
                return appointmentsRepository.findByDepartment_IdAndStatus(departmentId, status);
            }
            return appointmentsRepository.findByDepartmentId(departmentId);
        }

        if (status != null) {
            return appointmentsRepository.findByStatus(status);
        }

        if (startDate != null && endDate != null) {
            return appointmentsRepository.findByAppointmentDateBetween(startDate, endDate);
        }

        return getAllAppointments();
    }

    public List<Appointments> getAppointmentsByDoctor(Long doctorId) {
        return appointmentsRepository.findByDoctorId(doctorId);
    }

    public List<Appointments> getAppointmentsByDepartment(Long departmentId) {
        return appointmentsRepository.findByDepartmentId(departmentId);
    }

    public List<Appointments> getTodayAppointments() {
        return appointmentsRepository.findTodayAppointments();
    }

    public List<Appointments> getActiveAppointments() {
        return appointmentsRepository.findByStatus(AppointmentStatus.ACTIVE);
    }

    public List<Appointments> getCompletedAppointments() {
        return appointmentsRepository.findByStatus(AppointmentStatus.COMPLETED);
    }

    public List<Appointments> getUpcomingAppointments(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusDays(days);
        return appointmentsRepository.findByAppointmentDateBetween(now, endDate);
    }

    public List<Appointments> getUpcomingActiveAppointments() {
        return appointmentsRepository.findUpcomingActiveAppointments(LocalDateTime.now());
    }

    // ============ STATISTICS METHODS ============

    public Long countAllAppointments() {
        return appointmentsRepository.count();
    }

//    public Long countActiveAppointments() {
//        return appointmentsRepository.countByStatus(AppointmentStatus.ACTIVE);
//    }
//
//    public Long countCompletedAppointments() {
//        return appointmentsRepository.countByStatus(AppointmentStatus.COMPLETED);
//    }

    public Long countAppointmentsByDepartment(Long departmentId) {
        return appointmentsRepository.countByDepartment_Id(departmentId);
    }

    // ============ HELPER METHODS ============

    private void validateAppointmentData(Appointments appointment) {
        if (appointment.getAppointmentDate() == null) {
            throw new RuntimeException("Appointment date is required");
        }

        if (appointment.getPatientName() == null || appointment.getPatientName().trim().isEmpty()) {
            throw new RuntimeException("Patient name is required");
        }

        // Check if appointment date is in the past
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Appointment date cannot be in the past");
        }
    }

    public void autoCompletePastAppointments() {
        LocalDateTime now = LocalDateTime.now();
        List<Appointments> pastActiveAppointments = appointmentsRepository
                .findByAppointmentDateBefore(now)
                .stream()
                .filter(a -> a.getStatus() == AppointmentStatus.ACTIVE)
                .toList();

        for (Appointments appointment : pastActiveAppointments) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
            appointment.setUpdatedAt(now);
            appointmentsRepository.save(appointment);
        }
    }

    // Check doctor availability at specific time
    public boolean isDoctorAvailable(Long doctorId, LocalDateTime dateTime) {
        List<Appointments> conflictingAppointments = appointmentsRepository
                .findByDoctorId(doctorId)
                .stream()
                .filter(a -> a.getStatus() == AppointmentStatus.ACTIVE)
                .filter(a -> a.getAppointmentDate().equals(dateTime))
                .toList();

        return conflictingAppointments.isEmpty();
    }

    // Get appointments by status and date range
    public List<Appointments> getAppointmentsByStatusAndDateRange(AppointmentStatus status,
                                                                  LocalDateTime start,
                                                                  LocalDateTime end) {
        return appointmentsRepository.findByAppointmentDateBetween(start, end)
                .stream()
                .filter(a -> a.getStatus() == status)
                .toList();
    }
}