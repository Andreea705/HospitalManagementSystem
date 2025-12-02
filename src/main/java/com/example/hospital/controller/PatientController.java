package com.example.hospital.controller;

import com.example.hospital.model.Patient;
import com.example.hospital.model.Appointments;
import com.example.hospital.service.PatientService;
import com.example.hospital.service.AppointmentsService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final AppointmentsService appointmentsService;

    public PatientController(PatientService patientService,
                             AppointmentsService appointmentsService) {
        this.patientService = patientService;
        this.appointmentsService = appointmentsService;
    }

    // ============ LIST ALL PATIENTS (FIXED) ============
    @GetMapping
    public String findAll(Model model,
                          @RequestParam(required = false) String search,
                          @RequestParam(required = false) String sortBy,
                          @RequestParam(required = false) String sortOrder) {

        List<Patient> patients;

        if (search != null && !search.trim().isEmpty()) {
            patients = patientService.searchPatientsByName(search);
            model.addAttribute("search", search);
        } else {
            patients = patientService.getAllPatients();
        }

        // Simple sorting (you can enhance this)
        if ("name".equals(sortBy)) {
            if ("desc".equals(sortOrder)) {
                patients.sort((p1, p2) -> p2.getName().compareToIgnoreCase(p1.getName()));
            } else {
                patients.sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
            }
        } else if ("registrationDate".equals(sortBy)) {
            if ("desc".equals(sortOrder)) {
                patients.sort((p1, p2) -> p2.getRegistrationDate().compareTo(p1.getRegistrationDate()));
            } else {
                patients.sort((p1, p2) -> p1.getRegistrationDate().compareTo(p2.getRegistrationDate()));
            }
        }

        model.addAttribute("patients", patients);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("totalPatients", patients.size());

        return "patients/index";
    }

    // ============ VIEW DETAILS WITH APPOINTMENTS (FIXED - EAGER LOADING) ============
    @GetMapping("/{id}")
    public String viewDetails(@PathVariable Long id, Model model) {
        try {
            Patient patient = patientService.getPatientById(id);

            // The appointments should be loaded automatically by JPA
            // If they're not showing, we can fetch them explicitly
            List<Appointments> appointments = patient.getAppointments();

            model.addAttribute("patient", patient);
            model.addAttribute("appointments", appointments);
            model.addAttribute("totalAppointments", appointments != null ? appointments.size() : 0);
            model.addAttribute("activeAppointments",
                    appointments != null ? appointments.stream().filter(a -> a.isActive()).count() : 0);
            model.addAttribute("completedAppointments",
                    appointments != null ? appointments.stream().filter(a -> a.isCompleted()).count() : 0);
            model.addAttribute("upcomingAppointments",
                    appointments != null ? appointments.stream()
                            .filter(a -> a.isActive() && a.getAppointmentDate().isAfter(java.time.LocalDateTime.now()))
                            .count() : 0);

            return "patients/details";

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Patient not found: " + e.getMessage());
            return "redirect:/patients";
        }
    }

    // ============ SHOW CREATE FORM ============
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("minDate", LocalDate.now().minusYears(120)); // 120 years ago max
        model.addAttribute("maxDate", LocalDate.now()); // Can't be born in future
        return "patients/form";
    }

    // ============ CREATE PATIENT (WITH VALIDATION) ============
    @PostMapping
    public String save(@Valid @ModelAttribute Patient patient,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        // Custom validation for age
        if (patient.getDateOfBirth() != null && patient.getDateOfBirth().isAfter(LocalDate.now())) {
            bindingResult.rejectValue("dateOfBirth", "error.patient",
                    "Date of birth cannot be in the future");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("minDate", LocalDate.now().minusYears(120));
            model.addAttribute("maxDate", LocalDate.now());
            return "patients/form";
        }

        // Check for duplicate patient ID
        if (patientService.patientExistsByPatientId(patient.getPatientId())) {
            bindingResult.rejectValue("patientId", "error.patient",
                    "A patient with this Patient ID already exists");
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("minDate", LocalDate.now().minusYears(120));
            model.addAttribute("maxDate", LocalDate.now());
            return "patients/form";
        }

        // Check for duplicate email
        if (patientService.patientExistsByEmail(patient.getEmail())) {
            bindingResult.rejectValue("email", "error.patient",
                    "A patient with this email already exists");
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("minDate", LocalDate.now().minusYears(120));
            model.addAttribute("maxDate", LocalDate.now());
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
            model.addAttribute("minDate", LocalDate.now().minusYears(120));
            model.addAttribute("maxDate", LocalDate.now());
            return "patients/form";
        }
    }

    // ============ SHOW EDIT FORM ============
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Patient patient = patientService.getPatientById(id);
            model.addAttribute("patient", patient);
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("minDate", LocalDate.now().minusYears(120));
            model.addAttribute("maxDate", LocalDate.now());
            model.addAttribute("isEdit", true);
            return "patients/form";
        } catch (RuntimeException e) {
            return "redirect:/patients";
        }
    }

    // ============ UPDATE PATIENT (WITH VALIDATION) ============
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Patient patient,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        // Custom validation for age
        if (patient.getDateOfBirth() != null && patient.getDateOfBirth().isAfter(LocalDate.now())) {
            bindingResult.rejectValue("dateOfBirth", "error.patient",
                    "Date of birth cannot be in the future");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("minDate", LocalDate.now().minusYears(120));
            model.addAttribute("maxDate", LocalDate.now());
            model.addAttribute("isEdit", true);
            return "patients/form";
        }

        try {
            Patient existingPatient = patientService.getPatientById(id);

            // Check if patient ID is being changed and if new ID already exists
            if (!existingPatient.getPatientId().equals(patient.getPatientId()) &&
                    patientService.patientExistsByPatientId(patient.getPatientId())) {
                bindingResult.rejectValue("patientId", "error.patient",
                        "Patient ID is already taken by another patient");
                model.addAttribute("today", LocalDate.now());
                model.addAttribute("minDate", LocalDate.now().minusYears(120));
                model.addAttribute("maxDate", LocalDate.now());
                model.addAttribute("isEdit", true);
                return "patients/form";
            }

            // Check if email is being changed and if new email already exists
            if (!existingPatient.getEmail().equals(patient.getEmail()) &&
                    patientService.patientExistsByEmail(patient.getEmail())) {
                bindingResult.rejectValue("email", "error.patient",
                        "Email is already used by another patient");
                model.addAttribute("today", LocalDate.now());
                model.addAttribute("minDate", LocalDate.now().minusYears(120));
                model.addAttribute("maxDate", LocalDate.now());
                model.addAttribute("isEdit", true);
                return "patients/form";
            }

            patientService.updatePatient(id, patient);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Patient updated successfully!");
            return "redirect:/patients/" + id;
        } catch (RuntimeException e) {
            bindingResult.reject("error.patient", e.getMessage());
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("minDate", LocalDate.now().minusYears(120));
            model.addAttribute("maxDate", LocalDate.now());
            model.addAttribute("isEdit", true);
            return "patients/form";
        }
    }

    // ============ DELETE PATIENT (WITH APPOINTMENT CHECK) ============
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        try {
            Patient patient = patientService.getPatientById(id);

            // Check if patient has appointments
            List<Appointments> appointments = patient.getAppointments();
            if (appointments != null && !appointments.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Cannot delete patient with " + appointments.size() +
                                " appointment(s). Delete appointments first.");
                return "redirect:/patients/" + id;
            }

            patientService.deletePatient(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Patient deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error deleting patient: " + e.getMessage());
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

    // ============ GET PATIENT APPOINTMENTS PAGE ============
    @GetMapping("/{id}/appointments")
    public String getPatientAppointments(@PathVariable Long id, Model model) {
        try {
            Patient patient = patientService.getPatientById(id);
            List<Appointments> appointments = patient.getAppointments();

            model.addAttribute("patient", patient);
            model.addAttribute("appointments", appointments);
            model.addAttribute("totalAppointments", appointments != null ? appointments.size() : 0);

            return "patients/appointments";
        } catch (RuntimeException e) {
            return "redirect:/patients";
        }
    }

}