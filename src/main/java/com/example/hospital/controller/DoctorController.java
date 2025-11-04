package com.example.hospital.controller;

import com.example.hospital.model.Doctor;
import com.example.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Home page
    @GetMapping("/")
    public String home() {
        return "index"; // points to templates/index.html
    }

    // Doctors list page
    @GetMapping("/doctors")
    public String getAllDoctors(Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        return "doctors/index"; // points to templates/doctors/index.html
    }

    // Show create doctor form
    @GetMapping("/doctors/new")
    public String showCreateForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctors/form"; // points to templates/doctors/form.html
    }

    // Create new doctor
    @PostMapping("/doctors")
    public String createDoctor(@ModelAttribute Doctor doctor) {
        doctorService.save(doctor);
        return "redirect:/doctors";
    }

    // Delete doctor
    @PostMapping("/doctors/{id}/delete")
    public String deleteDoctor(@PathVariable String id) {
        doctorService.deleteById(id);
        return "redirect:/doctors";
    }
}
