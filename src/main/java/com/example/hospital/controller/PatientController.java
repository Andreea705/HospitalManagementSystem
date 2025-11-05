package com.example.hospital.controller;

import com.example.hospital.model.Patient;
import com.example.hospital.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patients/form";
    }

    @PostMapping
    public String save(@RequestParam String id,
                       @RequestParam String name,
                       @RequestParam String dateOfBirth,
                       @RequestParam String gender,
                       @RequestParam String emergencyContact) {

        try {
            Patient patient = new Patient();
            patient.setId(id);
            patient.setName(name);
            patient.setGender(gender);
            patient.setEmergencyContact(emergencyContact);

            if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date dob = dateFormat.parse(dateOfBirth);
                patient.setDateOfBirth(dob);
            }

            patientService.createPatient(patient);
            System.out.println("Patient saved successfully: " + patient.getName());

        } catch (Exception e) {
            System.out.println("Error saving patient: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/patients";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        patientService.deletePatient(id);
        return "redirect:/patients";
    }
}
