package com.example.hospital.repository;

import com.example.hospital.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByHospital_Id(Long hospitalId);

    @Query("SELECT d FROM Department d WHERE d.hospital.id = :hospitalId")
    List<Department> findByHospitalId(@Param("hospitalId") Long hospitalId);

    List<Department> findByHospital_IdOrderByNameAsc(Long hospitalId);

    List<Department> findByNameContainingIgnoreCase(String name);

    @Query("SELECT d FROM Department d WHERE d.name = :name AND d.hospital.id = :hospitalId")
    Optional<Department> findByNameAndHospitalId(@Param("name") String name,
                                                 @Param("hospitalId") Long hospitalId);

    @Query("SELECT COUNT(d) > 0 FROM Department d WHERE d.name = :name AND d.hospital.id = :hospitalId")
    boolean existsByNameAndHospitalId(@Param("name") String name,
                                      @Param("hospitalId") Long hospitalId);

    List<Department> findByDepartmentHeadContainingIgnoreCase(String departmentHead);

    @Query("SELECT d FROM Department d WHERE d.roomNumbers < 10")
    List<Department> findDepartmentsWithCapacity();

    @Query("SELECT COUNT(d) FROM Department d WHERE d.hospital.id = :hospitalId")
    Long countByHospitalId(@Param("hospitalId") Long hospitalId);


    @Query("SELECT d FROM Department d WHERE d.hospital.name LIKE %:hospitalName%")
    List<Department> findByHospitalNameContaining(@Param("hospitalName") String hospitalName);

    @Query("SELECT d FROM Department d WHERE d.roomNumbers > 0")
    List<Department> findDepartmentsWithRooms();
}