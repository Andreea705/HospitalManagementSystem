package com.example.hospital.controller;

import com.example.hospital.model.Appointments;
import com.example.hospital.model.AppointmentStatus;
import com.example.hospital.service.AppointmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appointments")
public class AppointmentsController {

    private final AppointmentsService appointmentsService;

    @Autowired
    public AppointmentsController(AppointmentsService appointmentsService) {
        this.appointmentsService = appointmentsService;
    }

    // Lista appointments
    @GetMapping
    public String getAllAppointments(Model model) {
            model.addAttribute("appointments", appointmentsService.findAll());
            return "appointments/index";
    }

    // Formular creare
    @GetMapping("/new")
    public String showCreateForm(Model model) {
            model.addAttribute("appointment", new Appointments());
            return "appointments/form";
    }

    // Formular editare
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
            Appointments appointment = appointmentsService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID: " + id));
            model.addAttribute("appointment", appointment);
            model.addAttribute("statuses", AppointmentStatus.values());
            return "appointments/form";
    }

    // Creare appointment nou
    @PostMapping
    public String createAppointment(@ModelAttribute Appointments appointment, Model model) {
        try {
            Appointments savedAppointment = appointmentsService.save(appointment);
            return "redirect:/appointments";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Could not create appointment: " + e.getMessage());
            model.addAttribute("appointment", appointment);
            model.addAttribute("statuses", AppointmentStatus.values());
            return "appointments/form";
        }
    }

    // Update appointment
    @PostMapping("/update/{id}")
    public String updateAppointment(@PathVariable String id, @ModelAttribute Appointments appointment, Model model) {
        try {
            appointment.setAppointmentId(id);
            Appointments updatedAppointment = appointmentsService.save(appointment);
            System.out.println("Updated appointment ID: " + updatedAppointment.getAppointmentId());
            return "redirect:/appointments";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Could not update appointment: " + e.getMessage());
            model.addAttribute("appointment", appointment);
            model.addAttribute("statuses", AppointmentStatus.values());
            return "appointments/form";
        }
    }

    // Ștergere
    @PostMapping("/{id}/delete")
    public String deleteAppointment(@PathVariable String id) {
            appointmentsService.deleteById(id);
            return "redirect:/appointments";
    }

    // Detalii
    @GetMapping("/{id}")
    public String findById(@PathVariable String id, Model model) {
        var appointment = appointmentsService.findById(id).orElse(null);
        model.addAttribute("appointment", appointment);
        return "appointments/details";
    }

//    @GetMapping("/{id}/exists")
//    @ResponseBody
//    public String existsById(@PathVariable String id) {
//        boolean exists = appointmentsService.existsById(id);
//        return exists ? "Appointment exists" : "Appointment not found";
//    }

//    @GetMapping("/department/{departmentId}")
//    public String findByDepartmentId(@PathVariable String departmentId, Model model) {
//        model.addAttribute("appointments", appointmentsService.findByDepartmentId(departmentId));
//        return "appointments/index";
//    }
}



