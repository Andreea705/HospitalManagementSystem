package com.example.hospital.controller;

import com.example.hospital.model.Doctor;
import com.example.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/doctors")
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

    // EDIT ENDPOINT - ADAUGĂ ASTA
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        System.out.println("🔄 EDIT FORM REQUESTED FOR DOCTOR ID: " + id);
        try {
            Optional<Doctor> doctor = doctorService.findById(id);
            if (doctor.isPresent()) {
                model.addAttribute("doctor", doctor.get());
                System.out.println("✅ DOCTOR FOUND: " + doctor.get().getMedicalStaffName());
                return "doctors/form";
            } else {
                System.out.println("❌ DOCTOR NOT FOUND");
                return "redirect:/doctors";
            }
        } catch (Exception e) {
            System.out.println("Error loading doctor for edit: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/doctors";
        }
    }

    @PostMapping
    public String createDoctor(@ModelAttribute Doctor doctor, Model model) {
        System.out.println("=== ATTEMPTING TO CREATE DOCTOR ===");
        System.out.println("MedicalStaffID: " + doctor.getMedicalStaffID());
        System.out.println("Name: " + doctor.getMedicalStaffName());
        System.out.println("License: " + doctor.getLicenseNumber());
        System.out.println("Specialization: " + doctor.getSpecialization());

        try {
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

    // UPDATE ENDPOINT - ADAUGĂ ASTA
    @PostMapping("/update/{id}")
    public String updateDoctor(@PathVariable String id, @ModelAttribute Doctor doctor, Model model) {
        System.out.println("=== ATTEMPTING TO UPDATE DOCTOR ===");
        System.out.println("Doctor ID: " + id);
        System.out.println("New Name: " + doctor.getMedicalStaffName());
        System.out.println("New License: " + doctor.getLicenseNumber());
        System.out.println("New Specialization: " + doctor.getSpecialization());

        try {
            doctorService.updateDoctor(id, doctor);
            System.out.println("✅ DOCTOR UPDATED SUCCESSFULLY!");
            return "redirect:/doctors";
        } catch (Exception e) {
            System.out.println(" ERROR UPDATING DOCTOR: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Could not update doctor: " + e.getMessage());
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