package com.example.hospital.controller;

import com.example.hospital.model.MedicalStaffAppointment;
import com.example.hospital.service.MedicalStaffAppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medical-staff-appointments")
public class MedicalStaffAppointmentController {

    private final MedicalStaffAppointmentService medicalStaffAppointmentService;

    public MedicalStaffAppointmentController(MedicalStaffAppointmentService medicalStaffAppointmentService) {
        this.medicalStaffAppointmentService = medicalStaffAppointmentService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("medicalStaffAppointments", medicalStaffAppointmentService.findAll());
        return "medicalStaffAppointment/index";  // Changed to singular
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("appointment", new MedicalStaffAppointment());
        return "medicalStaffAppointment/form";
    }

    @PostMapping
    public String save(@ModelAttribute MedicalStaffAppointment appointment) {
        medicalStaffAppointmentService.save(appointment);
        return "redirect:/medical-staff-appointments";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        medicalStaffAppointmentService.deleteById(id);
        return "redirect:/medical-staff-appointments";
    }
}

