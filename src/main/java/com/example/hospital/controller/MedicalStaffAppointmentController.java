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
        return "medicalStaffAppointments/index";
    }

    // Formular nou
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("appointment", new MedicalStaffAppointment());
        return "medicalStaffAppointments/form";
    }

    @PostMapping
    public String save(@ModelAttribute MedicalStaffAppointment appointment) {
        medicalStaffAppointmentService.save(appointment);
        return "redirect:/medical-staff-appointments";
    }

    // Formular editare
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        MedicalStaffAppointment appointment = medicalStaffAppointmentService.findById(id)
                .orElse(new MedicalStaffAppointment());
        model.addAttribute("appointment", appointment);
        return "medicalStaffAppointments/form";
    }

    // Actualizare programare
    @PostMapping("/update/{id}")
    public String update(@PathVariable String id, @ModelAttribute MedicalStaffAppointment appointment) {
        appointment.setAppointmentID(id); // Asigură că ID-ul este setat corect
        medicalStaffAppointmentService.save(appointment);
        return "redirect:/medical-staff-appointments";
    }

    // Ștergere programare
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        medicalStaffAppointmentService.deleteById(id);
        return "redirect:/medical-staff-appointments";
    }
}


