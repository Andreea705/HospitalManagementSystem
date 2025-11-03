package com.example.hospital.repository;
import com.example.hospital.model.Appointments;
import com.example.hospital.model.Hospital;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class AppointmentsRepo extends GenericRepo<Appointments, String> {

    @Override
    protected String getEntityId(Appointments appointments) {
        if (appointments.getAppointmentId() == null || appointments.getAppointmentId().isEmpty()) {
            return "APP_" + System.currentTimeMillis();
        }
        return appointments.getAppointmentId();
    }

    @Override
    protected String parseId(String id) {return id;}

    public List<Appointments> findByPatientID(String patientId) {
        return storage.values().stream()
                .filter(a -> a.getPatientId().equals(patientId))
                .toList();
    }
}
