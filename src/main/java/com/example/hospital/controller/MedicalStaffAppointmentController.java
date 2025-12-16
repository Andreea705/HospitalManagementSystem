package com.example.hospital.controller;

import com.example.hospital.model.MedicalStaffAppointment;
import com.example.hospital.service.MedicalStaffAppointmentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medical-staff-appointments")
public class MedicalStaffAppointmentController {

    private final MedicalStaffAppointmentService service;

    public MedicalStaffAppointmentController(MedicalStaffAppointmentService service) {
        this.service = service;
    }

    // ============ LIST ALL ============
//    @GetMapping
//    public String getAll(Model model) {
//        model.addAttribute("medicalStaffAppointments", service.findAll());
//        return "medicalStaffAppointment/index";
//    }

    // ============ LIST ALL CU SORTARE SI FILTRARE ============
    @GetMapping
    public String getAll(@RequestParam(required = false) String medicalStaffId,
                         @RequestParam(required = false) String appointmentId,
                         @RequestParam(defaultValue = "id") String sortBy,
                         @RequestParam(defaultValue = "asc") String sortDir,
                         Model model) {

        // Obține datele filtrate și sortate
        var appointments = service.filterAndSort(medicalStaffId, appointmentId, sortBy, sortDir);

        // Trimite datele către view
        model.addAttribute("medicalStaffAppointments", appointments);
        model.addAttribute("medicalStaffId", medicalStaffId);
        model.addAttribute("appointmentId", appointmentId);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        return "medicalStaffAppointment/index";
    }

    // ============ SHOW CREATE FORM ============
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("medicalStaffAppointment", new MedicalStaffAppointment());
        return "medicalStaffAppointment/form";
    }

    // ============ SHOW EDIT FORM ============
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MedicalStaffAppointment msa = service.findById(id);
        model.addAttribute("medicalStaffAppointment", msa);
        return "medicalStaffAppointment/form";
    }

    // ============ CREATE ============
    @PostMapping
    public String create(@Valid @ModelAttribute MedicalStaffAppointment medicalStaffAppointment,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "medicalStaffAppointment/form";
        }

        try {
            service.save(medicalStaffAppointment);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Assignment created successfully!");
            return "redirect:/medical-staff-appointments";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "medicalStaffAppointment/form";
        }
    }

    // ============ UPDATE ============
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute MedicalStaffAppointment medicalStaffAppointment,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "medicalStaffAppointment/form";
        }

        try {
            service.update(id, medicalStaffAppointment);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Assignment updated successfully!");
            return "redirect:/medical-staff-appointments";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "medicalStaffAppointment/form";
        }
    }

    // ============ DELETE ============
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Assignment deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/medical-staff-appointments";
    }

    @GetMapping("/{id}")
    public String viewDetails(@PathVariable Long id, Model model) {
        MedicalStaffAppointment msa = service.findById(id);

        msa.setAppointmentId("MSA_" + msa.getId());

        model.addAttribute("medicalStaffAppointment", msa);
        return "medicalStaffAppointment/details";
    }
}