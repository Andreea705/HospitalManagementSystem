package com.example.hospital.controller;

import com.example.hospital.model.Patient;
import com.example.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class ControllerPatient {

    private final PatientService service;

    @Autowired
    public ControllerPatient(PatientService service) {
        this.service = service;
    }

    @PostMapping
    public Patient create(@RequestBody Patient patient) {
        return service.createPatient(patient);
    }

    @GetMapping
    public List<Patient> getAll() {
        return service.getAllPatients();
    }

    @GetMapping("/{id}")
    public Patient getById(@PathVariable String id) {
        return service.getPatientById(id);
    }

    @PutMapping("/{id}")
    public Patient update(@PathVariable String id, @RequestBody Patient patient) {
        return service.updatePatient(id, patient);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deletePatient(id);
    }
}
