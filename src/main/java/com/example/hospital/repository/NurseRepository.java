package com.example.hospital.repository;

import com.example.hospital.model.Nurse;
import com.example.hospital.model.QualificationLevel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {

    //  find by medicalStaffId (String)
    Optional<Nurse> findByMedicalStaffId(String medicalStaffId);

    //  find by name
    List<Nurse> findByMedicalStaffNameContainingIgnoreCase(String name);

    //  find by department ID
    List<Nurse> findByDepartment_Id(Long departmentId);

    //  find by qualification level
    List<Nurse> findByQualificationLevel(QualificationLevel qualificationLevel);

    //  find by shift
    List<Nurse> findByShift(String shift);

    // find by duty status
    List<Nurse> findByOnDuty(boolean onDuty);

    //  find by department and duty status
    List<Nurse> findByDepartment_IdAndOnDuty(Long departmentId, boolean onDuty);

    //  find by department and shift
    List<Nurse> findByDepartment_IdAndShift(Long departmentId, String shift);

    // find by department and qualification
    List<Nurse> findByDepartment_IdAndQualificationLevel(Long departmentId, QualificationLevel qualificationLevel);

    // count by department
    Long countByDepartment_Id(Long departmentId);

    // count by duty status
    Long countByOnDuty(boolean onDuty);

    //  check if medicalStaffId exists
    boolean existsByMedicalStaffId(String medicalStaffId);

    //  search by multiple criteria
    @Query("""
SELECT n FROM Nurse n
LEFT JOIN n.department d
WHERE
  (:name IS NULL OR :name = '' OR LOWER(n.medicalStaffName) LIKE LOWER(CONCAT('%', :name, '%')))
  AND (:departmentId IS NULL OR n.department.id = :departmentId)
  AND (:qualification IS NULL OR n.qualificationLevel = :qualification)
  AND (:shift IS NULL OR :shift = '' OR n.shift = :shift)
  AND (:onDuty IS NULL OR n.onDuty = :onDuty)
""")
    List<Nurse> searchNurses(
            @Param("name") String name,
            @Param("departmentId") Long departmentId,
            @Param("qualification") QualificationLevel qualification,
            @Param("shift") String shift,
            @Param("onDuty") Boolean onDuty,
            Sort sort
    );


    // find available nurses (on duty and with department)
    @Query("SELECT n FROM Nurse n WHERE n.onDuty = true AND n.department IS NOT NULL")
    List<Nurse> findAvailableNurses();

    // find by name and department
    List<Nurse> findByMedicalStaffNameAndDepartment_Id(String medicalStaffName, Long departmentId);

    // find nurses without department
    @Query("SELECT n FROM Nurse n WHERE n.department IS NULL")
    List<Nurse> findNursesWithoutDepartment();
}