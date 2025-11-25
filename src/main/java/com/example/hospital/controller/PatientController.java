package com.example.hospital.controller;

import com.example.hospital.model.Patient;
import com.example.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Show main page with all patients (index.html)
    @GetMapping
    public String getAllPatients(Model model) {
        List<Patient> patients = patientService.getAllPatients();
        model.addAttribute("patients", patients);
        return "index";
    }

    // Show add patient form (form.html)
    @GetMapping("/new")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "form";
    }

    // Add new patient - FIXED DATE PARSING
    @PostMapping
    public String addPatient(@ModelAttribute Patient patient,
                             @RequestParam("dateOfBirthStr") String dateOfBirthStr) {
        try {
            // Convert date string to java.util.Date
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dateOfBirth = formatter.parse(dateOfBirthStr);
            patient.setDateOfBirth(dateOfBirth);

            patientService.createPatient(patient);
            return "redirect:/patients?success=Patient added successfully";
        } catch (Exception e) {
            return "redirect:/patients?error=Error adding patient: " + e.getMessage();
        }
    }
    // Show edit patient form (form.html)
    @GetMapping("/edit/{id}")
    public String showEditPatientForm(@PathVariable String id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "form";
    }

    @PostMapping("/update/{id}")
    public String updatePatient(@PathVariable String id,
                                @ModelAttribute Patient patient,
                                @RequestParam("dateOfBirthStr") String dateOfBirthStr) {
        try {
            // Convert date string to java.util.Date
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dateOfBirth = formatter.parse(dateOfBirthStr);
            patient.setDateOfBirth(dateOfBirth);

            // Make sure the ID is set from the path variable
            patient.setId(id);

            patientService.updatePatient(id, patient);
            return "redirect:/patients?success=Patient updated successfully";
        } catch (DateTimeParseException e) {
            return "redirect:/patients?error=Invalid date format. Please use DD/MM/YYYY";
        } catch (Exception e) {
            return "redirect:/patients?error=Error updating patient: " + e.getMessage();
        }
    }

    @GetMapping("/{id}")
    public String viewPatientDetails(@PathVariable String id, Model model) {
        Patient patient = patientService.getPatientById(id);

        model.addAttribute("patient", patient);
        model.addAttribute("appointments", patient.getAppointments());

        return "patient/details"; // make a patient/details.html file
    }


    // Delete patient
    @PostMapping("/{id}/delete")
    public String deletePatient(@PathVariable String id) {
        try {
            patientService.deletePatient(id);
            return "redirect:/patients?success=Patient deleted successfully";
        } catch (Exception e) {
            return "redirect:/patients?error=Error deleting patient: " + e.getMessage();
        }
    }
}