package com.example.hospital.controller;

import com.example.hospital.model.Appointments;
import com.example.hospital.model.AppointmentStatus;
import com.example.hospital.service.AppointmentsService;
import com.example.hospital.service.DepartmentService;
import com.example.hospital.service.DoctorService;
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

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Autowired
    public AppointmentsController(AppointmentsService appointmentsService,
                                  DepartmentService departmentService,
                                  DoctorService doctorService) {
        this.appointmentService = appointmentsService;
        this.departmentService = departmentService;
        this.doctorService = doctorService;
    }

    // ============ LIST ALL APPOINTMENTS ============

    @GetMapping
    public String getAllAppointments(@RequestParam(required = false) Long departmentId,
                                     @RequestParam(required = false) String status,
                                     @RequestParam(required = false) String patientName,
                                     @RequestParam(required = false) String startDate,
                                     @RequestParam(required = false) String endDate,
                                     Model model) {

        AppointmentStatus statusEnum = null;
        if (status != null && !status.isEmpty()) {
            try {
                statusEnum = AppointmentStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // statusul nu e valid
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
        }

        List<Appointments> appointments = appointmentService.searchAppointments(
                patientName, departmentId, statusEnum, startDateTime, endDateTime);

        model.addAttribute("appointments", appointments);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("statuses", AppointmentStatus.values());

        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("searchPatientName", patientName);
        model.addAttribute("searchStartDate", startDate);
        model.addAttribute("searchEndDate", endDate);

        // Statistici
        model.addAttribute("totalAppointments", appointmentService.countAllAppointments());
        //model.addAttribute("activeCount", appointmentService.countActiveAppointments());
        //model.addAttribute("completedCount", appointmentService.countCompletedAppointments());

        return "appointments/index";
    }

    // ============ SHOW CREATE FORM ============

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long departmentId,
                                 Model model) {
        Appointments appointment = new Appointments();
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0));

        model.addAttribute("appointment", appointment);
        model.addAttribute("departments", departmentService.getAllDepartments());

        // Încarca doctorii doar dacă exista un departament selectat
        if (departmentId != null) {
            model.addAttribute("doctors", doctorService.getDoctorsByDepartment(departmentId));
        } else {
            model.addAttribute("doctors", List.of());
        }

        model.addAttribute("selectedDepartmentId", departmentId);
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

    // ============ CREATE APPOINTMENT ============

    // În metodele createAppointment și updateAppointment:
    @PostMapping
    public String createAppointment(@Valid @ModelAttribute Appointments appointment,
                                    BindingResult bindingResult,
                                    @RequestParam Long departmentId,
                                    @RequestParam(required = false) Long doctorId,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("doctors", doctorService.getDoctorsByDepartment(departmentId));
            model.addAttribute("statuses", AppointmentStatus.values());
            model.addAttribute("today", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            return "appointments/form";
        }

        try {
            Appointments createdAppointment;
            if (doctorId != null) {
                createdAppointment = appointmentService.createAppointmentWithDoctor(appointment, departmentId, doctorId);
            } else {
                createdAppointment = appointmentService.createAppointment(appointment, departmentId);
            }

            redirectAttributes.addFlashAttribute("successMessage",
                    "Appointment created successfully for " + createdAppointment.getPatientName());

            return "redirect:/appointments?departmentId=" + departmentId;

        } catch (RuntimeException e) {
            bindingResult.reject("error.appointment", e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("doctors", doctorService.getDoctorsByDepartment(departmentId));
            model.addAttribute("statuses", AppointmentStatus.values());
            model.addAttribute("today", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            return "appointments/form";
        }
    }

    // ============ SHOW EDIT FORM ============

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Appointments appointment = appointmentService.getAppointmentById(id);

        model.addAttribute("appointment", appointment);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("statuses", AppointmentStatus.values());
        model.addAttribute("today", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));

        return "appointments/form";
    }

    // ============ UPDATE APPOINTMENT ============

    @PostMapping("/update/{id}")
    public String updateAppointment(@PathVariable Long id,
                                    @Valid @ModelAttribute Appointments appointment,
                                    BindingResult bindingResult,
                                    @RequestParam(required = false) Long doctorId,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("statuses", AppointmentStatus.values());
            model.addAttribute("today", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            return "appointments/form";
        }

        try {
            Appointments updatedAppointment = appointmentService.updateAppointment(id, appointment);

            if (doctorId != null) {
                appointmentService.assignDoctor(id, doctorId);
            }

            redirectAttributes.addFlashAttribute("successMessage",
                    "Appointment updated successfully for " + updatedAppointment.getPatientName());

            return "redirect:/appointments";

        } catch (RuntimeException e) {
            bindingResult.reject("error.appointment", e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("statuses", AppointmentStatus.values());
            model.addAttribute("today", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            return "appointments/form";
        }
    }

    // ============ DELETE APPOINTMENT ============

    @PostMapping("/{id}/delete")
    public String deleteAppointment(@PathVariable Long id,
                                    @RequestParam(required = false) Long departmentId,
                                    RedirectAttributes redirectAttributes) {

        try {
            Appointments appointment = appointmentService.getAppointmentById(id);
            appointmentService.deleteAppointment(id);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Appointment deleted successfully for " + appointment.getPatientName());

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (departmentId != null) {
            return "redirect:/appointments?departmentId=" + departmentId;
        }
        return "redirect:/appointments";
    }

    // ============ BUSINESS OPERATIONS ============

    @PostMapping("/{id}/complete")
    public String completeAppointment(@PathVariable Long id,
                                      @RequestParam(required = false) Long departmentId,
                                      RedirectAttributes redirectAttributes) {
        try {
            appointmentService.completeAppointment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment marked as completed");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (departmentId != null) {
            return "redirect:/appointments?departmentId=" + departmentId;
        }
        return "redirect:/appointments";
    }

    @PostMapping("/{id}/activate")
    public String activateAppointment(@PathVariable Long id,
                                      @RequestParam(required = false) Long departmentId,
                                      RedirectAttributes redirectAttributes) {
        try {
            appointmentService.activateAppointment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment activated");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (departmentId != null) {
            return "redirect:/appointments?departmentId=" + departmentId;
        }
        return "redirect:/appointments";
    }

    @PostMapping("/{id}/assign-doctor")
    public String assignDoctor(@PathVariable Long id,
                               @RequestParam Long doctorId,
                               @RequestParam(required = false) Long departmentId,
                               RedirectAttributes redirectAttributes) {
        try {
            appointmentService.assignDoctor(id, doctorId);
            redirectAttributes.addFlashAttribute("successMessage", "Doctor assigned successfully");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (departmentId != null) {
            return "redirect:/appointments?departmentId=" + departmentId;
        }
        return "redirect:/appointments";
    }

    @PostMapping("/{id}/remove-doctor")
    public String removeDoctor(@PathVariable Long id,
                               @RequestParam(required = false) Long departmentId,
                               RedirectAttributes redirectAttributes) {
        try {
            appointmentService.assignDoctor(id, null);
            redirectAttributes.addFlashAttribute("successMessage", "Doctor removed from appointment");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (departmentId != null) {
            return "redirect:/appointments?departmentId=" + departmentId;
        }
        return "redirect:/appointments";
    }

    // ============ SPECIAL VIEWS ============

    @GetMapping("/today")
    public String getTodayAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getTodayAppointments());
        model.addAttribute("viewTitle", "Today's Appointments");
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("statuses", AppointmentStatus.values());
        return "appointments/index";
    }

    @GetMapping("/active")
    public String getActiveAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getActiveAppointments());
        model.addAttribute("viewTitle", "Active Appointments");
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("statuses", AppointmentStatus.values());
        return "appointments/index";
    }

    @GetMapping("/completed")
    public String getCompletedAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getCompletedAppointments());
        model.addAttribute("viewTitle", "Completed Appointments");
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("doctors", doctorService.getAllDoctors());
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