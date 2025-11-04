package com.example.hospital.controller;

import com.example.hospital.model.MedicalStaffAppointment;
import com.example.hospital.service.MedicalStaffAppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medical-staff-appointments")
public class MedicalStaffAppointmentController {

    private final MedicalStaffAppointmentService appointmentService;

    public MedicalStaffAppointmentController(MedicalStaffAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("appointments", appointmentService.findAll());
        return "medicalStaffAppointments/index"; // → templates/medicalStaffAppointments/index.html
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable String id, Model model) {
        MedicalStaffAppointment appointment = appointmentService.findById(id).orElse(null);
        model.addAttribute("appointment", appointment);
        return "medicalStaffAppointments/details"; // → templates/medicalStaffAppointments/details.html
    }

//    @GetMapping("/new")
//    public String showCreateForm(Model model) {
//        model.addAttribute("appointment", new MedicalStaffAppointment());
//        return "medicalStaffAppointments/form"; // → templates/medicalStaffAppointments/form.html
//    }

    @PostMapping
    public String save(@ModelAttribute MedicalStaffAppointment appointment) {
        appointmentService.save(appointment);
        return "medical-staff-appointments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        MedicalStaffAppointment appointment = appointmentService.findById(id).orElse(null);
        model.addAttribute("appointment", appointment);
        return "medicalStaffAppointments/form"; // același form pentru edit
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable String id, @ModelAttribute MedicalStaffAppointment appointment) {
        appointmentService.save(appointment); //
        return "medical-staff-appointments";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        appointmentService.deleteById(id);
        return "medical-staff-appointments";
    }

    @GetMapping("/{id}/exists")
    public String existsById(@PathVariable String id, Model model) {
        boolean exists = appointmentService.existsById(id);
        model.addAttribute("existsMessage", exists ? "Appointment exists" : "Appointment not found");
        return "medicalStaffAppointments/exists"; //
    }
}


