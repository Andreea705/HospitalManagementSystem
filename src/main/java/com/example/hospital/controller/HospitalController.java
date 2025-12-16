package com.example.hospital.controller;

import com.example.hospital.model.Hospital;
import com.example.hospital.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }


    // ============ LIST ALL HOSPITALS ============
    @GetMapping
    public String findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        // Pass the 4 required arguments to the service
        model.addAttribute("hospitals", hospitalService.getAllHospitals(name, city, sortField, sortDir));

        // Return values to the model so the filter form retains them after submission
        model.addAttribute("name", name);
        model.addAttribute("city", city);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "hospitals/index";
    }

    // ============ SHOW CREATE FORM ============
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("hospital", new Hospital());
        return "hospitals/form";
    }

    // ============ VIEW DETAILS ============
    @GetMapping("/{id}")
    public String viewDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Hospital hospital = hospitalService.getHospitalById(id);
            model.addAttribute("hospital", hospital);
            return "hospitals/details";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/hospitals";
        }
    }

    // ============ CREATE HOSPITAL ============
    @PostMapping
    public String save(@Valid @ModelAttribute Hospital hospital,
                       BindingResult bindingResult,
                       Model model) {

        // 1. Backend-Validierungen (Format / Struktur)
        if (bindingResult.hasErrors()) {
            return "hospitals/form";
        }

        try {
            // 2. Business-Validierungen und Speicherung
            hospitalService.createHospital(hospital);
            return "redirect:/hospitals";

        } catch (RuntimeException e) {
            // Fängt RuntimeExceptions vom Service ab (z.B. "Name already exists")
            bindingResult.rejectValue("name", "error.hospital.name", e.getMessage());
            return "hospitals/form";
        }
    }

    // ============ SHOW EDIT FORM ============
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Hospital hospital = hospitalService.getHospitalById(id);
            model.addAttribute("hospital", hospital);
            return "hospitals/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/hospitals";
        }
    }

    // ============ UPDATE HOSPITAL ============
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Hospital hospital,
                         BindingResult bindingResult) {

        // 1. Backend-Validierungen (Format / Struktur)
        if (bindingResult.hasErrors()) {
            hospital.setId(id);
            return "hospitals/form";
        }

        try {
            // 2. Business-Validierungen und Update
            hospitalService.updateHospital(id, hospital);
            return "redirect:/hospitals";

        } catch (RuntimeException e) {
            // Fängt RuntimeExceptions vom Service ab (z.B. "Name already exists" oder "Hospital not found")

            // Wenn der Fehler den Namen betrifft (Uniqueness), verwenden wir das Feld 'name'
            if (e.getMessage().contains("Name")) {
                bindingResult.rejectValue("name", "error.hospital.name", e.getMessage());
            } else {
                // Genereller Fehler (z.B. ID nicht gefunden), verwenden wir das Feld 'id'
                bindingResult.rejectValue("id", "error.hospital.general", "Update failed: " + e.getMessage());
            }

            hospital.setId(id);
            return "hospitals/form";
        }
    }

    // ============ DELETE HOSPITAL ============
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            hospitalService.deleteHospital(id);
            redirectAttributes.addFlashAttribute("successMessage", "Hospital successfully deleted.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/hospitals";
    }
}