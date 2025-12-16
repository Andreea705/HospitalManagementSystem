package com.example.hospital.controller;

import com.example.hospital.model.*;
import com.example.hospital.service.AppointmentsService;
import com.example.hospital.service.DepartmentService;
import com.example.hospital.service.DoctorService;
import com.example.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final PatientService patientService;

    @Autowired
    public AppointmentsController(AppointmentsService appointmentsService,
                                  DepartmentService departmentService,
                                  DoctorService doctorService,
                                  PatientService patientService) {
        this.appointmentService = appointmentsService;
        this.departmentService = departmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // ============ LIST ALL APPOINTMENTS ============
    @GetMapping
    public String getAllAppointments(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) Long deptId,
            @RequestParam(defaultValue = "appointmentDate") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        model.addAttribute("appointments", appointmentService.getFilteredAndSorted(name, status, deptId, sortField, sortDir));

        model.addAttribute("statuses", AppointmentStatus.values());
        model.addAttribute("departments", departmentService.getAllDepartments());

        model.addAttribute("name", name);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedDeptId", deptId);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "appointments/index";
    }
    // ============ SHOW CREATE FORM ============
    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long departmentId,
                                 Model model) {

        Appointments appointment = new Appointments();
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0));
        appointment.setStatus(AppointmentStatus.ACTIVE);

        // Load doctors based on department
        List<Doctor> doctors;
        if (departmentId != null) {
            doctors = doctorService.getDoctorsByDepartment(departmentId);
        } else {
            doctors = doctorService.getAllDoctors();
        }

        model.addAttribute("appointment", appointment);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctors);
        model.addAttribute("statuses", AppointmentStatus.values());
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("today", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));

        return "appointments/form";
    }

    // ============ CREATE APPOINTMENT  ============
    @PostMapping
    public String createAppointment(@ModelAttribute Appointments appointment,
                                    @RequestParam Long patientId,
                                    @RequestParam Long departmentId,
                                    @RequestParam(required = false) Long doctorId,
                                    RedirectAttributes redirectAttributes) {

        try {
            // Get patient and department
            Patient patient = patientService.getPatientById(patientId);
            Department department = departmentService.getDepartmentById(departmentId);

            // Set values on appointment
            appointment.setPatient(patient);
            appointment.setPatientName(patient.getName()); // Setează patientName
            appointment.setDepartment(department);
            appointment.setStatus(AppointmentStatus.ACTIVE);

            // Set doctor if provided
            if (doctorId != null && doctorId > 0) {
                Doctor doctor = doctorService.getDoctorById(doctorId);
                appointment.setDoctor(doctor);
            }

            Appointments savedAppointment = appointmentService.saveAppointment(appointment);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Appointment created successfully!");

            return "redirect:/appointments";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error: " + e.getMessage());
            return "redirect:/appointments/new";
        }
    }

    // ============ SHOW EDIT FORM ============
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Appointments appointment = appointmentService.getAppointmentById(id);

        model.addAttribute("appointment", appointment);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("statuses", AppointmentStatus.values());
        model.addAttribute("today", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));

        return "appointments/form";
    }

    // ============ UPDATE APPOINTMENT ============
    @PostMapping("/update/{id}")
    public String updateAppointment(@PathVariable Long id,
                                    @ModelAttribute Appointments appointment,
                                    @RequestParam Long patientId,
                                    @RequestParam Long departmentId,
                                    @RequestParam(required = false) Long doctorId,
                                    RedirectAttributes redirectAttributes) {
        try {
            // 1. Die ID aus dem Pfad setzen
            appointment.setId(id);

            // 2. Die Relationen laden und setzen (WICHTIG für die Validierung im Service)
            Patient patient = patientService.getPatientById(patientId);
            Department department = departmentService.getDepartmentById(departmentId);

            appointment.setPatient(patient);
            appointment.setDepartment(department);

            if (doctorId != null && doctorId > 0) {
                Doctor doctor = doctorService.getDoctorById(doctorId);
                appointment.setDoctor(doctor);
            } else {
                appointment.setDoctor(null);
            }

            // 3. Den Service aufrufen
            appointmentService.saveAppointment(appointment);

            redirectAttributes.addFlashAttribute("successMessage", "Appointment updated successfully!");
            return "redirect:/appointments";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Update failed: " + e.getMessage());
            return "redirect:/appointments/edit/" + id;
        }
    }
    // ============ DELETE APPOINTMENT ============
    @PostMapping("/{id}/delete")
    public String deleteAppointment(@PathVariable Long id,
                                    RedirectAttributes redirectAttributes) {
        try {
            appointmentService.deleteAppointment(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Appointment deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error: " + e.getMessage());
        }
        return "redirect:/appointments";
    }

    // ============ SHOW APPOINTMENT DETAILS ============
    @GetMapping("/{id}")
    public String getAppointmentDetails(@PathVariable Long id, Model model) {
        try {
            Appointments appointment = appointmentService.getAppointmentById(id);
            List<Doctor> doctors = doctorService.getAllDoctors();

            model.addAttribute("appointment", appointment);
            model.addAttribute("doctors", doctors);
            return "appointments/details";
        } catch (Exception e) {
            // Redirect la listă cu mesaj de eroare
            return "redirect:/appointments";
        }
    }
}