package com.example.hospital.controller;

import com.example.hospital.model.Doctor;
import com.example.hospital.service.ServiceDoctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class ControllerDoctor {

    private final ServiceDoctor service;

    @Autowired
    public ControllerDoctor(ServiceDoctor service) {
        this.service = service;
    }

    @PostMapping
    public Doctor create(@RequestBody Doctor doctor) {
        return service.createDoctor(doctor);
    }

    @GetMapping
    public List<Doctor> getAll() {
        return service.getAllDoctors();
    }

    @GetMapping("/{license}")
    public Doctor getByLicense(@PathVariable String license) {
        return service.getDoctorByLicenseNumber(license);
    }

    @PutMapping("/{license}")
    public Doctor update(@PathVariable String license, @RequestBody Doctor doctor) {
        return service.updateDoctor(license, doctor);
    }

    @DeleteMapping("/{license}")
    public void delete(@PathVariable String license) {
        service.deleteDoctor(license);
    }
}

