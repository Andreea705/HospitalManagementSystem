package com.example.hospital.controller;

import com.example.hospital.model.MedicalStaffAppointment;
import com.example.hospital.service.ServiceMedicalStaffAppointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-staff-appointments")
public class ControllerMedicalStaffAppointment {

    private final ServiceMedicalStaffAppointment service;

    @Autowired
    public ControllerMedicalStaffAppointment(ServiceMedicalStaffAppointment service) {
        this.service = service;
    }

    @PostMapping
    public MedicalStaffAppointment create(@RequestBody MedicalStaffAppointment appointment) {
        return service.createMedicalStaffAppointment(appointment);
    }

    @GetMapping
    public List<MedicalStaffAppointment> getAll() {
        return service.getAllMedicalStaffAppointments();
    }

    @GetMapping("/{id}")
    public MedicalStaffAppointment getById(@PathVariable String id) {
        return service.getMedicalStaffAppointmentById(id);
    }

    @PutMapping("/{id}")
    public MedicalStaffAppointment update(@PathVariable String id, @RequestBody MedicalStaffAppointment appointment) {
        return service.updateMedicalStaffAppointment(id, appointment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteMedicalStaffAppointment(id);
    }
}

