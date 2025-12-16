package com.example.hospital.repository;

import com.example.hospital.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Cautare dupa medicalStaffId
    List<Doctor> findByMedicalStaffId(String medicalStaffId);

    // Cautare dupa department (entitate)
    @Query("SELECT d FROM Doctor d WHERE d.department.id = :departmentId")
    List<Doctor> findByDepartmentId(@Param("departmentId") Long departmentId);

    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);

    // Cautare dupa nume
    List<Doctor> findByMedicalStaffNameContainingIgnoreCase(String name);

    // Cautare dupa license number
    Doctor findByLicenseNumber(String licenseNumber);

    // Cautare dupa email
    Doctor findByEmail(String email);

    // Gasește doctorii fara departament asignat
    @Query("SELECT d FROM Doctor d WHERE d.department IS NULL")
    List<Doctor> findDoctorsWithoutDepartment();

    // Cautare avansata
//    @Query("SELECT d FROM Doctor d WHERE " +
//            "(:name IS NULL OR LOWER(d.medicalStaffName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
//            "(:specialization IS NULL OR LOWER(d.specialization) LIKE LOWER(CONCAT('%', :specialization, '%'))) AND " +
//            "(:departmentId IS NULL OR d.department.id = :departmentId)")
////    List<Doctor> searchDoctors(@Param("name") String name,
////                               @Param("specialization") String specialization,
////                               @Param("departmentId") Long departmentId);

    @Query("""
    SELECT d FROM Doctor d
    WHERE
      (:name IS NULL OR LOWER(d.medicalStaffName) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:specialization IS NULL OR LOWER(d.specialization) LIKE LOWER(CONCAT('%', :specialization, '%')))
      AND (:departmentId IS NULL OR d.department.id = :departmentId)
""")
    List<Doctor> searchDoctors(
            @Param("name") String name,
            @Param("specialization") String specialization,
            @Param("departmentId") Long departmentId,
            org.springframework.data.domain.Sort sort
    );


    // Verifica existența dupa medicalStaffId
    boolean existsByMedicalStaffId(String medicalStaffId);

    // Verifica existența dupa licenseNumber
    boolean existsByLicenseNumber(String licenseNumber);
}