package com.example.hospital.repository;

import com.example.hospital.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//le face JpaRepository toate operatiile crud aparent
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    // Optional: Add custom query methods if needed
    boolean existsByName(String name);
}