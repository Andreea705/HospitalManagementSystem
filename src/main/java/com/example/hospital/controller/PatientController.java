package com.example.hospital.controller;

import com.example.hospital.model.Patient;
import com.example.hospital.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // ============ LIST ALL PATIENTS ============
    @GetMapping
    public String findAll(Model model,
                          @RequestParam(required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("patients", patientService.searchPatientsByName(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("patients", patientService.getAllPatients());
        }
        return "patients/index";
    }

    // ============ SHOW CREATE FORM ============
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("today", LocalDate.now());
        return "patients/form";
    }

    // ============ VIEW DETAILS ============
    @GetMapping("/{id}")
    public String viewDetails(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patients/details";
    }

    // ============ CREATE PATIENT ============
    @PostMapping
    public String save(@Valid @ModelAttribute Patient patient,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        // Field validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("today", LocalDate.now());
            return "patients/form";
        }

        // Business validation: Check unique patientId
        if (patientService.patientExistsByPatientId(patient.getPatientId())) {
            bindingResult.rejectValue("patientId", "error.patient",
                    "A patient with this Patient ID already exists");
            model.addAttribute("today", LocalDate.now());
            return "patients/form";
        }

        // Business validation: Check unique email
        if (patientService.patientExistsByEmail(patient.getEmail())) {
            bindingResult.rejectValue("email", "error.patient",
                    "A patient with this email already exists");
            model.addAttribute("today", LocalDate.now());
            return "patients/form";
        }

        try {
            patientService.createPatient(patient);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Patient created successfully!");
            return "redirect:/patients";
        } catch (RuntimeException e) {
            bindingResult.reject("error.patient", e.getMessage());
            model.addAttribute("today", LocalDate.now());
            return "patients/form";
        }
    }

    // ============ SHOW EDIT FORM ============
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        model.addAttribute("today", LocalDate.now());
        return "patients/form";
    }

    // ============ UPDATE PATIENT ============
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Patient patient,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("today", LocalDate.now());
            return "patients/form";
        }

        try {
            // Get existing patient to check if fields changed
            Patient existingPatient = patientService.getPatientById(id);

            // Check if patientId changed and is now taken by someone else
            if (!existingPatient.getPatientId().equals(patient.getPatientId()) &&
                    patientService.patientExistsByPatientId(patient.getPatientId())) {
                bindingResult.rejectValue("patientId", "error.patient",
                        "Patient ID is already taken by another patient");
                model.addAttribute("today", LocalDate.now());
                return "patients/form";
            }

            // Check if email changed and is now taken by someone else
            if (!existingPatient.getEmail().equals(patient.getEmail()) &&
                    patientService.patientExistsByEmail(patient.getEmail())) {
                bindingResult.rejectValue("email", "error.patient",
                        "Email is already used by another patient");
                model.addAttribute("today", LocalDate.now());
                return "patients/form";
            }

            patientService.updatePatient(id, patient);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Patient updated successfully!");
            return "redirect:/patients";
        } catch (RuntimeException e) {
            bindingResult.reject("error.patient", e.getMessage());
            model.addAttribute("today", LocalDate.now());
            return "patients/form";
        }
    }

    // ============ DELETE PATIENT ============
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        try {
            patientService.deletePatient(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Patient deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/patients";
    }

    // ============ AJAX VALIDATION ENDPOINTS ============
    @GetMapping("/check-patient-id")
    @ResponseBody
    public String checkPatientId(@RequestParam String patientId,
                                 @RequestParam(required = false) Long currentId) {
        Optional<Patient> patient = patientService.getPatientByPatientId(patientId);

        if (patient.isPresent() && (currentId == null || !patient.get().getId().equals(currentId))) {
            return "taken";
        }
        return "available";
    }

    @GetMapping("/check-email")
    @ResponseBody
    public String checkEmail(@RequestParam String email,
                             @RequestParam(required = false) Long currentId) {
        boolean exists = patientService.patientExistsByEmail(email);

        if (exists) {
            // Check if it's the same patient
            Patient patient = patientService.getAllPatients().stream()
                    .filter(p -> p.getEmail().equals(email))
                    .findFirst()
                    .orElse(null);

            if (patient != null && (currentId == null || !patient.getId().equals(currentId))) {
                return "taken";
            }
        }
        return "available";
    }
}