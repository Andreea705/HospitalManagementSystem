package com.example.hospital.repository;

import com.example.hospital.model.Appointments;
import com.example.hospital.model.AppointmentStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
//marcheaza interfata ca spring repo
public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {


    @Query("SELECT a FROM Appointments a WHERE a.doctor.id = :doctorId")
    List<Appointments> findByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT a FROM Appointments a WHERE DATE(a.appointmentDate) = CURRENT_DATE " +
            "ORDER BY a.appointmentDate")
    List<Appointments> findTodayAppointments();

    @Query("SELECT a FROM Appointments a WHERE LOWER(a.patientName) LIKE LOWER(CONCAT('%', :patientName, '%'))")
    List<Appointments> searchByPatientName(@Param("patientName") String patientName);

    @Query("SELECT COUNT(a) > 0 FROM Appointments a " +
            "WHERE a.doctor.id = :doctorId " +
            "AND a.appointmentDate > :now " +
            "AND a.status = 'ACTIVE'")
    boolean hasFutureActiveAppointments(@Param("doctorId") Long doctorId,
                                        @Param("now") LocalDateTime now);

    List<Appointments> findByDepartment_IdAndStatus(Long departmentId, AppointmentStatus status);


    @Query("SELECT a FROM Appointments a WHERE a.patient.id = :patientId")
    List<Appointments> findByPatientId(@Param("patientId") Long patientId);

    //sortarea dupa completed/active + nume
    @Query("SELECT a FROM Appointments a WHERE " +
            "(:name IS NULL OR LOWER(a.patientName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:status IS NULL OR a.status = :status) AND " +
            "(:deptId IS NULL OR a.department.id = :deptId)")
    List<Appointments> findByFilters(@Param("name") String name,
                                     @Param("status") AppointmentStatus status,
                                     @Param("deptId") Long deptId,
                                     Sort sort);;

    List<Appointments> findByPatientNameContainingIgnoreCase(String patientName, Sort sort);

    @Query("SELECT COUNT(a) FROM Appointments a WHERE a.patient.id = :patientId")
    Long countByPatientId(@Param("patientId") Long patientId);
}