package com.example.hospital.controller;

import com.example.hospital.model.Doctor;
import com.example.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/doctors")  // Add this to group all doctor mappings
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }


    @GetMapping
    public String getAllDoctors(Model model) {
        System.out.println("Doctors list accessed");
        try {
            model.addAttribute("doctors", doctorService.findAll());
            System.out.println("Doctors found: " + doctorService.findAll().size());
        } catch (Exception e) {
            System.out.println("Error getting doctors: " + e.getMessage());
            e.printStackTrace();
        }
        return "doctors/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        System.out.println("Doctor form accessed");
        try {
            model.addAttribute("doctor", new Doctor());
            System.out.println("New doctor object created");
        } catch (Exception e) {
            System.out.println("Error creating doctor form: " + e.getMessage());
            e.printStackTrace();
        }
        return "doctors/form";
    }

    @PostMapping
    public String createDoctor(@ModelAttribute Doctor doctor, Model model) {
        System.out.println("=== ATTEMPTING TO CREATE DOCTOR ===");
        System.out.println("MedicalStaffID: " + doctor.getMedicalStaffID());
        System.out.println("Name: " + doctor.getMedicalStaffName());
        System.out.println("License: " + doctor.getLicenseNumber());
        System.out.println("Specialization: " + doctor.getSpecialization());

        try {
            // Set a default role if not set
            if (doctor.getRole() == null) {
                doctor.setRole("doctor");
            }

            Doctor savedDoctor = doctorService.save(doctor);
            System.out.println("✅ DOCTOR SAVED SUCCESSFULLY!");
            System.out.println("Saved with ID: " + savedDoctor.getMedicalStaffID());

            return "redirect:/doctors";
        } catch (Exception e) {
            System.out.println(" ERROR SAVING DOCTOR: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Could not save doctor: " + e.getMessage());
            model.addAttribute("doctor", doctor);
            return "doctors/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteDoctor(@PathVariable String id) {
        System.out.println("Deleting doctor with ID: " + id);
        try {
            doctorService.deleteById(id);
            System.out.println("Doctor deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting doctor: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/doctors";
    }
}
