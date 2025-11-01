package com.example.hospital.service;

import com.example.hospital.model.Appointments;
import com.example.hospital.repository.AppointmentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentsService {
    private final AppointmentsRepo appointmentsRepo;

    @Autowired
    public AppointmentsService(AppointmentsRepo appointmentsRepo) {
        this.appointmentsRepo = appointmentsRepo;
    }

    public Appointments createAppointment(Appointments appointment) {
        validateAppointment(appointment);
        return appointmentsRepo.save(appointment);
    }

    public List<Appointments> getAllAppointments() {
        return appointmentsRepo.findAllAppointments();
    }

    public Appointments getAppointmentById(String appointmentId) {
        Appointments appointment = appointmentsRepo.findbyAppointmentID(appointmentId);
        if (appointment == null) {
            throw new RuntimeException("Appointment not found with ID: " + appointmentId);
        }
        return appointment;
    }

    public Appointments updateAppointment(String appointmentId, Appointments updatedAppointment) {
        Appointments existingAppointment = getAppointmentById(appointmentId);

        existingAppointment.setPatientId(updatedAppointment.getPatientId());
        existingAppointment.setStatus(updatedAppointment.getStatus());
        existingAppointment.setAdmissionDate(updatedAppointment.getAdmissionDate());
        existingAppointment.setDepartmentId(updatedAppointment.getDepartmentId());

        validateAppointment(existingAppointment);
        return appointmentsRepo.save(existingAppointment);
    }

    public boolean deleteAppointment(String appointmentId) {
        return appointmentsRepo.deleteByAppointmentID(appointmentId);
    }


    private void validateAppointment(Appointments appointment) {
        if (appointment.getAppointmentId() == null || appointment.getAppointmentId().trim().isEmpty()) {
            throw new IllegalArgumentException("Appointment ID cannot be empty");
        }
        if (appointment.getPatientId() == null || appointment.getPatientId().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient ID cannot be empty");
        }
        if (appointment.getDepartmentId() == null || appointment.getDepartmentId().trim().isEmpty()) {
            throw new IllegalArgumentException("Department ID cannot be empty");
        }
        if (appointment.getAdmissionDate() == null) {
            throw new IllegalArgumentException("Admission date cannot be empty");
        }
    }


    public List<Appointments> getAppointmentsByPatient(String patientId) {
        return appointmentsRepo.findByPatientID(patientId);
    }

    public List<Appointments> getAppointmentsByStatus(String status) {
        return getAllAppointments().stream()
                .filter(appointment -> status.equals(appointment.getStatus()))
                .toList();
    }

    public List<Appointments> getAppointmentsByDepartment(String departmentId) {
        return getAllAppointments().stream()
                .filter(appointment -> departmentId.equals(appointment.getDepartmentId()))
                .toList();
    }

    public boolean appointmentExists(String appointmentId) {
        return appointmentsRepo.findbyAppointmentID(appointmentId) != null;
    }

    public void cancelAppointment(String appointmentId) {
        Appointments appointment = getAppointmentById(appointmentId);
        appointment.setStatus("Cancelled");
        appointmentsRepo.save(appointment);
    }

    public void completeAppointment(String appointmentId) {
        Appointments appointment = getAppointmentById(appointmentId);
        appointment.setStatus("Completed");
        appointmentsRepo.save(appointment);
    }

    public boolean canRescheduleAppointment(String appointmentId, LocalDate newDate) {
        Appointments appointment = getAppointmentById(appointmentId);
        return newDate.isAfter(LocalDate.now())
                && ("Scheduled".equals(appointment.getStatus()) || "Confirmed".equals(appointment.getStatus()));
    }

    public long getTotalAppointmentCount() {
        return getAllAppointments().size();
    }

    public long getAppointmentCountByStatus(String status) {
        return getAppointmentsByStatus(status).size();
    }

    public AppointmentStatistics getAppointmentStatistics() {
        List<Appointments> allAppointments = getAllAppointments();

        long scheduled = allAppointments.stream().filter(a -> "Scheduled".equals(a.getStatus())).count();
        long completed = allAppointments.stream().filter(a -> "Completed".equals(a.getStatus())).count();
        long cancelled = allAppointments.stream().filter(a -> "Cancelled".equals(a.getStatus())).count();

        return new AppointmentStatistics(
                allAppointments.size(),
                scheduled,
                completed,
                cancelled
        );
    }

    public static class AppointmentStatistics {
        private final long total;
        private final long scheduled;
        private final long completed;
        private final long cancelled;

        public AppointmentStatistics(long total, long scheduled, long completed, long cancelled) {
            this.total = total;
            this.scheduled = scheduled;
            this.completed = completed;
            this.cancelled = cancelled;
        }

        public long getTotal() { return total; }
        public long getScheduled() { return scheduled; }
        public long getCompleted() { return completed; }
        public long getCancelled() { return cancelled; }
    }
}
