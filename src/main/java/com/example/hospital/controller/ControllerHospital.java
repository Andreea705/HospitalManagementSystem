package com.example.hospital.controller;

import com.example.hospital.model.Hospital;
import com.example.hospital.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
public class ControllerHospital {

    private final HospitalService service;

    @Autowired
    public ControllerHospital(HospitalService service) {
        this.service = service;
    }

    @PostMapping
    public Hospital create(@RequestBody Hospital hospital) {
        return service.createHospital(hospital);
    }

    @GetMapping
    public List<Hospital> getAll() {
        return service.getAllHospitals();
    }

    @GetMapping("/{id}")
    public Hospital getById(@PathVariable String id) {
        return service.getHospitalById(id);
    }

    @PutMapping("/{id}")
    public Hospital update(@PathVariable String id, @RequestBody Hospital hospital) {
        return service.updateHospital(id, hospital);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteHospital(id);
    }
}

