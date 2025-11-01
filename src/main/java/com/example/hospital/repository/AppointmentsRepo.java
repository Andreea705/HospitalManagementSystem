package com.example.hospital.repository;
import com.example.hospital.model.Appointments;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;

@Repository
public class AppointmentsRepo {
    private final List<Appointments> appointments = new ArrayList<>();

    public Appointments save(Appointments appointment) {
        Appointments existingAppointment = findbyAppointmentID(appointment.getAppointmentId());

        if (existingAppointment != null) {
            existingAppointment.setAppointmentId(appointment.getAppointmentId());
            existingAppointment.setPatientId(appointment.getPatientId());
            existingAppointment.setStatus(appointment.getStatus());
            existingAppointment.setAdmissionDate(appointment.getAdmissionDate());
            existingAppointment.setDepartmentId(appointment.getDepartmentId());
            return existingAppointment;

        }
        else{
            appointments.add(appointment);
            return appointment;
        }

    }

    public List<Appointments> findAllAppointments() {return new ArrayList<>(appointments);}

    public Appointments findbyAppointmentID(String appointmentID){
        if(appointmentID == null) return null;

        for (Appointments appointment : appointments) {
            if(appointment.getAppointmentId().equals(appointmentID))
                {return appointment;}
        }
        return null;
    }

    public boolean deleteByAppointmentID(String appointmentID){
        Appointments appointment = findbyAppointmentID(appointmentID);
        if (appointment != null) {
            appointments.remove(appointment);
        }
        return true;
    }

    public List<Appointments> findByPatientID(String patientID){
        List<Appointments> result = new ArrayList<>();
        for (Appointments appointment : appointments) {
            if(appointment != null && patientID.equals(appointment.getPatientId())){
                result.add(appointment);
            }
        }
        return result;
    }

    public boolean existsByAppointmentID(String appointmentID){return findbyAppointmentID(appointmentID) != null;}
}
