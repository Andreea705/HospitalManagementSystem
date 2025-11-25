package com.example.hospital.controller;

import com.example.hospital.model.Patient;
import com.example.hospital.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        return "patients/index"; // Changed
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patients/form"; // Changed
    }

    @GetMapping("/{id}")
    public String viewDetails(@PathVariable String id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patients/details"; // Changed
    }

    @PostMapping
    public String save(@ModelAttribute Patient patient,
                       @RequestParam("dateOfBirthStr") String dateOfBirthStr) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dateOfBirth = formatter.parse(dateOfBirthStr);
            patient.setDateOfBirth(dateOfBirth);
            patientService.createPatient(patient);
            return "redirect:/patients?success=Patient added successfully";
        } catch (Exception e) {
            return "redirect:/patients?error=Error adding patient: " + e.getMessage();
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patients/form"; // Changed
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable String id,
                         @ModelAttribute Patient patient,
                         @RequestParam("dateOfBirthStr") String dateOfBirthStr) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dateOfBirth = formatter.parse(dateOfBirthStr);
            patient.setDateOfBirth(dateOfBirth);
            patient.setId(id);
            patientService.updatePatient(id, patient);
            return "redirect:/patients?success=Patient updated successfully";
        } catch (Exception e) {
            return "redirect:/patients?error=Error updating patient: " + e.getMessage();
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        try {
            patientService.deletePatient(id);
            return "redirect:/patients?success=Patient deleted successfully";
        } catch (Exception e) {
            return "redirect:/patients?error=Error deleting patient: " + e.getMessage();
        }
    }
}