package com.example.hospital.repository;

import com.example.hospital.model.Appointments;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppointmentsRepository extends InFileRepository<Appointments, String> {

    public AppointmentsRepository() {
        super("appointments.json", Appointments.class);
    }

    @Override
    protected String getEntityId(Appointments appointment) {
        if (appointment.getAppointmentId() == null || appointment.getAppointmentId().isEmpty()) {
            String newId = "APP_" + System.currentTimeMillis();
            setEntityId(appointment, newId);
            return newId;
        }
        return appointment.getAppointmentId();
    }

    @Override
    protected void setEntityId(Appointments appointment, String id) {
        appointment.setAppointmentId(id);
    }

    @Override
    protected String parseId(String id) {
        return id;
    }

    public List<Appointments> findByPatientID(String patientId) {
        return findAll().stream()
                .filter(a -> patientId.equals(a.getPatientId()))
                .toList();
    }
}