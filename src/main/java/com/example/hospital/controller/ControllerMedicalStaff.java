package com.example.hospital.controller;

import com.example.hospital.model.MedicalStaff;
import com.example.hospital.service.ServiceMedicalStaff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-staff")
public class ControllerMedicalStaff {

    private final ServiceMedicalStaff service;

    @Autowired
    public ControllerMedicalStaff(ServiceMedicalStaff service) {
        this.service = service;
    }

    @PostMapping
    public MedicalStaff create(@RequestBody MedicalStaff staff) {
        return service.createMedicalStaff(staff);
    }

    @GetMapping
    public List<MedicalStaff> getAll() {
        return service.getAllMedicalStaff();
    }

    @GetMapping("/{id}")
    public MedicalStaff getById(@PathVariable String id) {
        return service.getMedicalStaffById(id);
    }

    @PutMapping("/{id}")
    public MedicalStaff update(@PathVariable String id, @RequestBody MedicalStaff staff) {
        return service.updateMedicalStaff(id, staff);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteMedicalStaff(id);
    }
}

