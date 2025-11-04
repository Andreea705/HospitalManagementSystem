package com.example.hospital.controller;

import com.example.hospital.model.Appointments;
import com.example.hospital.model.AppointmentStatus;
import com.example.hospital.service.AppointmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AppointmentsController {

    private final AppointmentsService appointmentsService;

    @Autowired
    public AppointmentsController(AppointmentsService appointmentsService) {
        this.appointmentsService = appointmentsService;
    }

    // ✅ Lista appointments - la fel ca getAllDoctors()
    @GetMapping("/appointments")
    public String getAllAppointments(Model model) {
        System.out.println("Appointments list accessed");
        try {
            model.addAttribute("appointments", appointmentsService.findAll());
            System.out.println("Appointments found: " + appointmentsService.findAll().size());
        } catch (Exception e) {
            System.out.println("Error getting appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return "appointments/index";
    }

    // ✅ Formular creare - la fel ca showCreateForm()
    @GetMapping("/appointments/new")
    public String showCreateForm(Model model) {
        System.out.println("Appointment form accessed");
        try {
            model.addAttribute("appointment",
                    new Appointments("", "", "", "", AppointmentStatus.ACTIVE.name()));
            model.addAttribute("statuses", AppointmentStatus.values());
            System.out.println("New appointment object created");
        } catch (Exception e) {
            System.out.println("Error creating appointment form: " + e.getMessage());
            e.printStackTrace();
        }
        return "appointments/form";
    }

    // ✅ Salvare - la fel ca createDoctor()
    @PostMapping("/appointments")
    public String createAppointment(@ModelAttribute Appointments appointment, Model model) {
        System.out.println("=== ATTEMPTING TO CREATE APPOINTMENT ===");
        System.out.println("Appointment ID: " + appointment.getAppointmentId());
        System.out.println("Patient ID: " + appointment.getPatientId());
        System.out.println("Department ID: " + appointment.getDepartmentId());
        System.out.println("Admission Date: " + appointment.getAdmissionDate());
        System.out.println("Status: " + appointment.getStatus());

        try {
            Appointments savedAppointment = appointmentsService.save(appointment);
            System.out.println("✅ APPOINTMENT SAVED SUCCESSFULLY!");
            System.out.println("Saved with ID: " + savedAppointment.getAppointmentId());

            return "redirect:/appointments";
        } catch (Exception e) {
            System.out.println("❌ ERROR SAVING APPOINTMENT: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Could not save appointment: " + e.getMessage());
            model.addAttribute("appointment", appointment);
            model.addAttribute("statuses", AppointmentStatus.values());
            return "appointments/form";
        }
    }

    // ✅ Ștergere - la fel ca deleteDoctor()
    @PostMapping("/appointments/{id}/delete")
    public String deleteAppointment(@PathVariable String id) {
        System.out.println("Deleting appointment with ID: " + id);
        try {
            appointmentsService.deleteById(id);
            System.out.println("Appointment deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/appointments";
    }

    // ✅ Păstrează și metodele tale existente dacă vrei
    @GetMapping("/appointments/{id}")
    public String findById(@PathVariable String id, Model model) {
        var appointment = appointmentsService.findById(id).orElse(null);
        model.addAttribute("appointment", appointment);
        return "appointments/details";
    }

    @GetMapping("/appointments/{id}/exists")
    public String existsById(@PathVariable String id) {
        boolean exists = appointmentsService.existsById(id);
        return exists ? "Appointment exists" : "Appointment not found";
    }

    @GetMapping("/appointments/department/{departmentId}")
    public String findByDepartmentId(@PathVariable String departmentId, Model model) {
        model.addAttribute("appointments", appointmentsService.findByDepartmentId(departmentId));
        return "appointments/index";
    }
}



