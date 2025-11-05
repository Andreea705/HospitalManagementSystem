package com.example.hospital.repository;

import com.example.hospital.model.MedicalStaffAppointment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicalStaffAppointmentRepo extends GenericRepo<MedicalStaffAppointment, String> {

    @Override
    protected String getEntityId(MedicalStaffAppointment entity) {
        if (entity.getMedicalStaffAppointmentId() == null || entity.getMedicalStaffAppointmentId().isEmpty()) {
            return "MSA_" + System.currentTimeMillis();
        }
        return entity.getMedicalStaffAppointmentId();
    }

    @Override
    protected String parseId(String id) {
        return id;
    }

    public List<MedicalStaffAppointment> findByMedicalStaffId(String medicalStaffId) {
        return storage.values().stream()
                .filter(m -> m.getMedicalStaffId().equals(medicalStaffId))
                .toList();
    }

    public List<MedicalStaffAppointment> findByAppointmentID(String appointmentID) {
        return storage.values().stream()
                .filter(m -> m.getAppointmentID().equals(appointmentID))
                .toList();
    }
}