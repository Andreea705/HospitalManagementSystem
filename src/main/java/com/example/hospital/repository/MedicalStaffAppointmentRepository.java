package com.example.hospital.repository;

import com.example.hospital.model.MedicalStaffAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalStaffAppointmentRepository extends JpaRepository<MedicalStaffAppointment, Long> {

    List<MedicalStaffAppointment> findByMedicalStaffId(String medicalStaffId);

    List<MedicalStaffAppointment> findByAppointmentId(String appointmentId);

    boolean existsByMedicalStaffIdAndAppointmentId(String medicalStaffId, String appointmentId);

    //cautare combinata dupa medicalstaffid si appointmentid
    List<MedicalStaffAppointment> findByMedicalStaffIdAndAppointmentId(String medicalStaffId, String appointmentId);

    //like - cautare ambele campuri
    @Query("SELECT msa FROM MedicalStaffAppointment msa WHERE " +
            "(:medicalStaffId IS NULL OR msa.medicalStaffId LIKE %:medicalStaffId%) AND " +
            "(:appointmentId IS NULL OR msa.appointmentId LIKE %:appointmentId%)")
    List<MedicalStaffAppointment> search(@Param("medicalStaffId") String medicalStaffId,
                                         @Param("appointmentId") String appointmentId);
}