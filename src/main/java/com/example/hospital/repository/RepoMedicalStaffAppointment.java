package com.example.hospital.repository;

import com.example.hospital.model.MedicalStaffAppointment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;

@Repository
public class RepoMedicalStaffAppointment {

    private final List<MedicalStaffAppointment> medicalStaffAppointmentList = new ArrayList<>();

    public MedicalStaffAppointment save (MedicalStaffAppointment staffAppointment) {
        MedicalStaffAppointment existingmedicalStaffAppointment = findByMedicalStaffAppointmentID(existingmedicalStaffAppointment.getMedicalStaffAppointmentId());

        if (existingmedicalStaffAppointment != null) {
            existingmedicalStaffAppointment.setMedicalStaffAppointmentId(staffAppointment.getMedicalStaffAppointmentId());
            existingmedicalStaffAppointment.setMedicalStaffId(staffAppointment.getMedicalStaffId());
            existingmedicalStaffAppointment.setAppointmentID(staffAppointment.getAppointmentID());
        } else {
            medicalStaffAppointmentList.add(staffAppointment);
            return staffAppointment;
        }
        return staffAppointment;
    }

    public List<MedicalStaffAppointment> findByMedicalStaffAppointmentID(String medicalStaffID) {
        return new ArrayList<>(medicalStaffAppointmentList);
    }

    public MedicalStaffAppointment findByMedicalStaffAppointmentId(String id) {
        if (id == null) return null;

        for (MedicalStaffAppointment appointment : medicalStaffAppointmentList) {
            if (appointment.getMedicalStaffAppointmentId().equals(id)) {
                return appointment;
            }
        }
        return null;
    }

    public boolean deleteByMedicalStaffAppointmentId(String id) {
        MedicalStaffAppointment appointment = findByMedicalStaffAppointmentId(id);
        if (appointment != null) {
            medicalStaffAppointmentList.remove(appointment);
            return true;
        }
        return false;
    }

    public boolean existsByMedicalStaffAppointmentId(String id) {
        return findByMedicalStaffAppointmentId(id) != null;
    }

    public List<MedicalStaffAppointment> findByMedicalStaffId(String medicalStaffId) {
        List<MedicalStaffAppointment> result = new ArrayList<>();
        for (MedicalStaffAppointment appointment : medicalStaffAppointmentList) {
            if (appointment != null && appointment.getMedicalStaffId().equals(medicalStaffId)) {
                result.add(appointment);
            }
        }
        return result;
    }

    public List<MedicalStaffAppointment> findByAppointmentID(String appointmentID) {
        List<MedicalStaffAppointment> result = new ArrayList<>();
        for (MedicalStaffAppointment appointment : medicalStaffAppointmentList) {
            if (appointment != null && appointment.getAppointmentID().equals(appointmentID)) {
                result.add(appointment);
            }
        }
        return result;
    }
}

