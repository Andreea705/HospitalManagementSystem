package com.example.hospital.repository;

import com.example.hospital.model.Hospital;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//le face JpaRepository toate operatiile crud aparent
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {


    boolean existsByName(String name);
    Hospital findByName(String name);

    boolean existsByNameAndCity(String name, String city);
    Hospital findByNameAndCity(String name, String city);
    List<Hospital> findByNameContainingIgnoreCaseAndCityContainingIgnoreCase(
            String name, String city, Sort sort);

}