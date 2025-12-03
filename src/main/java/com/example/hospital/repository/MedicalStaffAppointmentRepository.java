package com.example.hospital.repository;

import com.example.hospital.model.MedicalStaffAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalStaffAppointmentRepository extends JpaRepository<MedicalStaffAppointment, Long> {

    List<MedicalStaffAppointment> findByMedicalStaffId(String medicalStaffId);

    List<MedicalStaffAppointment> findByAppointmentId(String appointmentId);

    boolean existsByMedicalStaffIdAndAppointmentId(String medicalStaffId, String appointmentId);
}