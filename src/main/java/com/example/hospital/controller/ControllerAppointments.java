package com.example.hospital.controller;

import com.example.hospital.model.Appointments;
import com.example.hospital.service.ServiceAppointments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
class AppointmentsController {

    private final ServiceAppointments serviceAppointments;

    @Autowired
    public AppointmentsController(ServiceAppointments service) {
        this.serviceAppointments = service;
    }

    // create-adaugă o programare
    @PostMapping
    public Appointments create(@RequestBody Appointments appointment) {
        return serviceAppointments.createAppointment(appointment);
    }

    // preia toate programările
    @GetMapping
    public List<Appointments> getAll() {
        return serviceAppointments.getAllAppointments();
    }

    // preia o programare după ID
    @GetMapping("/{id}")
    public Appointments getById(@PathVariable String id) {
        return serviceAppointments.getAppointmentById(id);
    }

    // modifică o programare după ID
    @PutMapping("/{id}")
    public Appointments update(@PathVariable String id, @RequestBody Appointments appointment) {
        return serviceAppointments.updateAppointment(id, appointment);
    }

    // șterge o programare după ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        serviceAppointments.deleteAppointment(id);
    }
}
