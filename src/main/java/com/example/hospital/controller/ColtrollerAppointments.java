package com.example.hospital.controller;

import com.example.hospital.model.Appointments;
import com.example.hospital.service.AppointmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class ColtrollerAppointments {

    private final AppointmentsService service;

    @Autowired
    public ColtrollerAppointments(AppointmentsService service) {
        this.service = service;
    }

    @PostMapping
    public Appointments create(@RequestBody Appointments appointment) {
        return service.createAppointment(appointment);
    }

    @GetMapping
    public List<Appointments> getAll() {
        return service.getAllAppointments();
    }

    @GetMapping("/{id}")
    public Appointments getById(@PathVariable String id) {
        return service.getAppointmentById(id);
    }

    @PutMapping("/{id}")
    public Appointments update(@PathVariable String id, @RequestBody Appointments appointment) {
        return service.updateAppointment(id, appointment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteAppointment(id);
    }
}

