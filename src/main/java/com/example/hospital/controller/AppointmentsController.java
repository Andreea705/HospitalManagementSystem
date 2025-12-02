package com.example.hospital.controller;

import com.example.hospital.model.Appointments;
import com.example.hospital.model.AppointmentStatus;
import com.example.hospital.model.Patient;
import com.example.hospital.service.AppointmentsService;
import com.example.hospital.service.DepartmentService;
import com.example.hospital.service.DoctorService;
import com.example.hospital.service.PatientService; // ADD THIS
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentsController {

    private final AppointmentsService appointmentService;
    private final DepartmentService departmentService;
    private final DoctorService doctorService;
    private final PatientService patientService; // ADD THIS

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Autowired
    public AppointmentsController(AppointmentsService appointmentsService,
                                  DepartmentService departmentService,
                                  DoctorService doctorService,
                                  PatientService patientService) { // ADD THIS PARAMETER
        this.appointmentService = appointmentsService;
        this.departmentService = departmentService;
        this.doctorService = doctorService;
        this.patientService = patientService; // INITIALIZE
    }

    // ============ LIST ALL APPOINTMENTS ============
    @GetMapping
    public String getAllAppointments(@RequestParam(required = false) Long departmentId,
                                     @RequestParam(required = false) String status,
                                     @RequestParam(required = false) Long patientId, // ADD THIS
                                     @RequestParam(required = false) String patientName,
                                     @RequestParam(required = false) String startDate,
                                     @RequestParam(required = false) String endDate,
                                     Model model) {

        AppointmentStatus statusEnum = null;
        if (status != null && !status.isEmpty()) {
            try {
                statusEnum = AppointmentStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Invalid status
            }
        }

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        try {
            if (startDate != null && !startDate.isEmpty()) {
                startDateTime = LocalDateTime.parse(startDate, formatter);
            }
            if (endDate != null && !endDate.isEmpty()) {
                endDateTime = LocalDateTime.parse(endDate, formatter);
            }
        } catch (Exception e) {
            // Date parsing error
        }

        List<Appointments> appointments = appointmentService.searchAppointments(
                patientName, departmentId, statusEnum, startDateTime, endDateTime);

        // If patientId is specified, filter by patient
        if (patientId != null) {
            appointments = appointments.stream()
                    .filter(a -> a.getPatient() != null && a.getPatient().getId().equals(patientId))
                    .toList();
        }

        model.addAttribute("appointments", appointments);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patients", patientService.getAllPatients()); // ADD THIS
        model.addAttribute("statuses", AppointmentStatus.values());

        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("selectedPatientId", patientId); // ADD THIS
        model.addAttribute("selectedStatus", status);
        model.addAttribute("searchPatientName", patientName);
        model.addAttribute("searchStartDate", startDate);
        model.addAttribute("searchEndDate", endDate);

        // Statistics
        model.addAttribute("totalAppointments", appointmentService.countAllAppointments());

        return "appointments/index";
    }

    // ============ SHOW CREATE FORM (UPDATED) ============
    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long departmentId,
                                 @RequestParam(required = false) Long patientId, // ADD THIS
                                 Model model) {
        Appointments appointment = new Appointments();
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0));

        // Set patient if provided
        if (patientId != null) {
            try {
                Patient patient = patientService.getPatientById(patientId);
                appointment.setPatient(patient);
                appointment.setPatientName(patient.getName());
            } catch (RuntimeException e) {
                // Patient not found, continue without
            }
        }

        model.addAttribute("appointment", appointment);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("patients", patientService.getAllPatients()); // ADD THIS

        // Load doctors only if department is selected
        if (departmentId != null) {
            model.addAttribute("doctors", doctorService.getDoctorsByDepartment(departmentId));
        } else {
            model.addAttribute("doctors", List.of());
        }

        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("selectedPatientId", patientId); // ADD THIS
        model.addAttribute("statuses", AppointmentStatus.values());
        model.addAttribute("today", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));

        return "appointments/form";
    }

    // ============ VIEW APPOINTMENT DETAILS ============
    @GetMapping("/{id}")
    public String getAppointmentById(@PathVariable Long id, Model model) {
        Appointments appointment = appointmentService.getAppointmentById(id);
        model.addAttribute("appointment", appointment);
        return "appointments/details";
    }

    // ============ CREATE APPOINTMENT (UPDATED) ============
    @PostMapping
    public String createAppointment(@Valid @ModelAttribute("appointment") Appointments appointment,
                                    BindingResult bindingResult,
                                    @RequestParam(required = false) Long departmentId,
                                    @RequestParam(required = false) Long doctorId,
                                    @RequestParam(required = false) Long patientId, // ADD THIS
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        // Validate department
        if (departmentId == null && appointment.getDepartment() != null) {
            departmentId = appointment.getDepartment().getId();
        }

        if (departmentId == null) {
            bindingResult.rejectValue("department", "error.appointment", "Department is required");
        }

        // Validate patient
        if (patientId == null && appointment.getPatient() == null) {
            bindingResult.rejectValue("patient", "error.appointment", "Patient is required");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("patients", patientService.getAllPatients()); // ADD THIS
            model.addAttribute("doctors", doctorService.getDoctorsByDepartment(departmentId));
            model.addAttribute("statuses", AppointmentStatus.values());
            model.addAttribute("today", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            model.addAttribute("selectedDepartmentId", departmentId);
            model.addAttribute("selectedPatientId", patientId);
            return "appointments/form";
        }

        try {
            Appointments createdAppointment;

            // If patientId is provided but patient object is not set
            if (appointment.getPatient() == null && patientId != null) {
                Patient patient = patientService.getPatientById(patientId);
                appointment.setPatient(patient);
            }

            // Get the final patient ID for redirection
            Long finalPatientId = appointment.getPatient() != null ?
                    appointment.getPatient().getId() : patientId;

            if (doctorId != null) {
                createdAppointment = appointmentService.createAppointmentWithDoctor(
                        appointment, departmentId, doctorId, finalPatientId);
            } else {
                createdAppointment = appointmentService.createAppointment(
                        appointment, departmentId, finalPatientId);
            }

            redirectAttributes.addFlashAttribute("successMessage",
                    "Appointment created successfully for " + createdAppointment.getPatientName());

            // Redirect to patient's details page
            return "redirect:/patients/" + finalPatientId;

        } catch (RuntimeException e) {
            bindingResult.reject("error.appointment", e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("patients", patientService.getAllPatients()); // ADD THIS
            model.addAttribute("doctors", doctorService.getDoctorsByDepartment(departmentId));
            model.addAttribute("statuses", AppointmentStatus.values());
            model.addAttribute("today", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            model.addAttribute("selectedDepartmentId", departmentId);
            model.addAttribute("selectedPatientId", patientId);
            return "appointments/form";
        }
    }

    // ============ SHOW EDIT FORM (UPDATED) ============
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Appointments appointment = appointmentService.getAppointmentById(id);

        model.addAttribute("appointment", appointment);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patients", patientService.getAllPatients()); // ADD THIS
        model.addAttribute("statuses", AppointmentStatus.values());
        model.addAttribute("today", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));

        return "appointments/form";
    }

    // ============ UPDATE APPOINTMENT (UPDATED) ============
    @PostMapping("/update/{id}")
    public String updateAppointment(@PathVariable Long id,
                                    @Valid @ModelAttribute("appointment") Appointments appointment,
                                    BindingResult bindingResult,
                                    @RequestParam(required = false) Long departmentId,
                                    @RequestParam(required = false) Long doctorId,
                                    @RequestParam(required = false) Long patientId, // ADD THIS
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        if (departmentId == null && appointment.getDepartment() != null) {
            departmentId = appointment.getDepartment().getId();
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("patients", patientService.getAllPatients()); // ADD THIS
            model.addAttribute("statuses", AppointmentStatus.values());
            model.addAttribute("today", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            return "appointments/form";
        }

        try {
            // Set patient if patientId is provided
            if (patientId != null && appointment.getPatient() == null) {
                Patient patient = patientService.getPatientById(patientId);
                appointment.setPatient(patient);
            }

            Appointments updatedAppointment = appointmentService.updateAppointment(id, appointment);

            if (doctorId != null) {
                appointmentService.assignDoctor(id, doctorId);
            }

            redirectAttributes.addFlashAttribute("successMessage",
                    "Appointment updated successfully for " + updatedAppointment.getPatientName());

            // Redirect to patient's details page
            Long redirectPatientId = updatedAppointment.getPatient() != null ?
                    updatedAppointment.getPatient().getId() : patientId;
            if (redirectPatientId != null) {
                return "redirect:/patients/" + redirectPatientId;
            }

            return "redirect:/appointments";

        } catch (RuntimeException e) {
            bindingResult.reject("error.appointment", e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("patients", patientService.getAllPatients()); // ADD THIS
            model.addAttribute("statuses", AppointmentStatus.values());
            model.addAttribute("today", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            return "appointments/form";
        }
    }

    // ============ DELETE APPOINTMENT (UPDATED) ============
    @PostMapping("/{id}/delete")
    public String deleteAppointment(@PathVariable Long id,
                                    @RequestParam(required = false) Long departmentId,
                                    @RequestParam(required = false) Long patientId, // ADD THIS
                                    RedirectAttributes redirectAttributes) {

        try {
            Appointments appointment = appointmentService.getAppointmentById(id);
            Long patientIdToRedirect = appointment.getPatient() != null ?
                    appointment.getPatient().getId() : patientId;

            appointmentService.deleteAppointment(id);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Appointment deleted successfully for " + appointment.getPatientName());

            // Redirect to patient's page if patient exists
            if (patientIdToRedirect != null) {
                return "redirect:/patients/" + patientIdToRedirect;
            }

            if (departmentId != null) {
                return "redirect:/appointments?departmentId=" + departmentId;
            }
            return "redirect:/appointments";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/appointments";
        }
    }

    // ============ BUSINESS OPERATIONS (UPDATED WITH PATIENT REDIRECT) ============
    @PostMapping("/{id}/complete")
    public String completeAppointment(@PathVariable Long id,
                                      @RequestParam(required = false) Long departmentId,
                                      @RequestParam(required = false) Long patientId, // ADD THIS
                                      RedirectAttributes redirectAttributes) {
        try {
            appointmentService.completeAppointment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment marked as completed");

            // Redirect to patient's page if patientId provided
            if (patientId != null) {
                return "redirect:/patients/" + patientId;
            }

            if (departmentId != null) {
                return "redirect:/appointments?departmentId=" + departmentId;
            }
            return "redirect:/appointments";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/appointments";
        }
    }

    @PostMapping("/{id}/activate")
    public String activateAppointment(@PathVariable Long id,
                                      @RequestParam(required = false) Long departmentId,
                                      @RequestParam(required = false) Long patientId, // ADD THIS
                                      RedirectAttributes redirectAttributes) {
        try {
            appointmentService.activateAppointment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment activated");

            // Redirect to patient's page if patientId provided
            if (patientId != null) {
                return "redirect:/patients/" + patientId;
            }

            if (departmentId != null) {
                return "redirect:/appointments?departmentId=" + departmentId;
            }
            return "redirect:/appointments";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/appointments";
        }
    }

    @PostMapping("/{id}/assign-doctor")
    public String assignDoctor(@PathVariable Long id,
                               @RequestParam Long doctorId,
                               @RequestParam(required = false) Long departmentId,
                               @RequestParam(required = false) Long patientId, // ADD THIS
                               RedirectAttributes redirectAttributes) {
        try {
            appointmentService.assignDoctor(id, doctorId);
            redirectAttributes.addFlashAttribute("successMessage", "Doctor assigned successfully");

            // Redirect to patient's page if patientId provided
            if (patientId != null) {
                return "redirect:/patients/" + patientId;
            }

            if (departmentId != null) {
                return "redirect:/appointments?departmentId=" + departmentId;
            }
            return "redirect:/appointments";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/appointments";
        }
    }

    @PostMapping("/{id}/remove-doctor")
    public String removeDoctor(@PathVariable Long id,
                               @RequestParam(required = false) Long departmentId,
                               @RequestParam(required = false) Long patientId, // ADD THIS
                               RedirectAttributes redirectAttributes) {
        try {
            appointmentService.assignDoctor(id, null);
            redirectAttributes.addFlashAttribute("successMessage", "Doctor removed from appointment");

            // Redirect to patient's page if patientId provided
            if (patientId != null) {
                return "redirect:/patients/" + patientId;
            }

            if (departmentId != null) {
                return "redirect:/appointments?departmentId=" + departmentId;
            }
            return "redirect:/appointments";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/appointments";
        }
    }

    // ============ SPECIAL VIEWS ============
    // (These can stay the same as they don't need patient parameter)
    @GetMapping("/today")
    public String getTodayAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getTodayAppointments());
        model.addAttribute("viewTitle", "Today's Appointments");
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patients", patientService.getAllPatients()); // ADD THIS
        model.addAttribute("statuses", AppointmentStatus.values());
        return "appointments/index";
    }

    @GetMapping("/active")
    public String getActiveAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getActiveAppointments());
        model.addAttribute("viewTitle", "Active Appointments");
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patients", patientService.getAllPatients()); // ADD THIS
        model.addAttribute("statuses", AppointmentStatus.values());
        return "appointments/index";
    }

    @GetMapping("/completed")
    public String getCompletedAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getCompletedAppointments());
        model.addAttribute("viewTitle", "Completed Appointments");
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patients", patientService.getAllPatients()); // ADD THIS
        model.addAttribute("statuses", AppointmentStatus.values());
        return "appointments/index";
    }

    @GetMapping("/upcoming")
    public String getUpcomingAppointments(@RequestParam(defaultValue = "7") int days,
                                          Model model) {
        model.addAttribute("appointments", appointmentService.getUpcomingAppointments(days));
        model.addAttribute("viewTitle", "Upcoming Appointments (next " + days + " days)");
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patients", patientService.getAllPatients()); // ADD THIS
        model.addAttribute("statuses", AppointmentStatus.values());
        return "appointments/index";
    }

    @PostMapping("/auto-complete")
    public String autoCompletePastAppointments(RedirectAttributes redirectAttributes) {
        try {
            appointmentService.autoCompletePastAppointments();
            redirectAttributes.addFlashAttribute("successMessage",
                    "Past active appointments have been marked as completed");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/appointments";
    }
}