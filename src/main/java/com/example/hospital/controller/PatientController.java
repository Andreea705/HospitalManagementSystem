package com.example.hospital.controller;

import com.example.hospital.model.Patient;
import com.example.hospital.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "patients/index";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable String id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patients/details";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patients/form"; // → templates/patients/form.html
    }

    @PostMapping
    public String save(@ModelAttribute Patient patient) {
        patientService.createPatient(patient);
        return "patients";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patients/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable String id, @ModelAttribute Patient patient) {
        patientService.updatePatient(id, patient);
        return "patients";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        patientService.deletePatient(id);
        return "patients";
    }

    @GetMapping("/{id}/exists")
    public String existsById(@PathVariable String id, Model model) {
        boolean exists = patientService.getAllPatients().stream()
                .anyMatch(p -> p.getId().equals(id));
        model.addAttribute("existsMessage", exists ? "Patient exists" : "Patient not found");
        return "exists";
    }
}

