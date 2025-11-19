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
        Patient patient = new Patient("", "", new Date(), "", "");
        model.addAttribute("patient", patient);
        return "patients/form";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patients/form";
    }

    @PostMapping
    public String save(@ModelAttribute Patient patient,
                       @RequestParam String dateOfBirthStr) {

        try {
            if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date dob = dateFormat.parse(dateOfBirthStr);
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


    @PostMapping("/update/{id}")
    public String updatePatient(@PathVariable String id,
                                @ModelAttribute Patient updatedPatient,
                                @RequestParam String dateOfBirthStr) {

        try {
            if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date dob = dateFormat.parse(dateOfBirthStr);
                updatedPatient.setDateOfBirth(dob);
            }

            updatedPatient.setId(id);
            patientService.updatePatient(id, updatedPatient);
            System.out.println("Patient updated successfully: " + updatedPatient.getName());

        } catch (Exception e) {
            System.out.println("Error updating patient: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/patients";
    }

    @GetMapping("/{id}")
    public String showPatientDetails(@PathVariable String id, Model model) {
        try {
            Patient patient = patientService.getPatientById(id);
            model.addAttribute("patient", patient);
            return "patients/details";
        } catch (RuntimeException e) {
            return "redirect:/patients?error=Patient not found";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        patientService.deletePatient(id);
        return "redirect:/patients";
    }
}