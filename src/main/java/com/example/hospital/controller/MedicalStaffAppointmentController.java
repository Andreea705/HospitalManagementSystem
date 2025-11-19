package com.example.hospital.controller;

import com.example.hospital.model.MedicalStaffAppointment;
import com.example.hospital.service.MedicalStaffAppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/medical-staff-appointments")
public class MedicalStaffAppointmentController {

    private final MedicalStaffAppointmentService medicalStaffAppointmentService;

    public MedicalStaffAppointmentController(MedicalStaffAppointmentService medicalStaffAppointmentService) {
        this.medicalStaffAppointmentService = medicalStaffAppointmentService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("medicalStaffAppointments", medicalStaffAppointmentService.findAll());
        return "medicalStaffAppointment/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("appointment", new MedicalStaffAppointment());
        return "medicalStaffAppointment/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        System.out.println("EDIT FORM REQUESTED FOR ID: " + id);
        Optional<MedicalStaffAppointment> appointment = medicalStaffAppointmentService.findById(id);
        if (appointment.isPresent()) {
            model.addAttribute("appointment", appointment.get());
            System.out.println("APPOINTMENT FOUND: " + appointment.get().getMedicalStaffAppointmentId());
            return "medicalStaffAppointment/form";
        } else {
            System.out.println("APPOINTMENT NOT FOUND");
            return "redirect:/medical-staff-appointments";
        }
    }

    @PostMapping("/save")
    public String saveOrUpdate(@ModelAttribute MedicalStaffAppointment appointment) {
        System.out.println("SAVE/UPDATE CALLED");
        System.out.println("ID: " + appointment.getMedicalStaffAppointmentId());
        System.out.println("MedicalStaffId: " + appointment.getMedicalStaffId());
        System.out.println("AppointmentID: " + appointment.getAppointmentID());

        if (appointment.getMedicalStaffAppointmentId() != null &&
                !appointment.getMedicalStaffAppointmentId().isEmpty()) {
            // UPDATE existing appointment
            System.out.println("UPDATING EXISTING APPOINTMENT");
            medicalStaffAppointmentService.updateMedicalStaffAppointment(
                    appointment.getMedicalStaffAppointmentId(),
                    appointment
            );
        } else {
            // CREATE new appointment
            System.out.println("CREATING NEW APPOINTMENT");
            medicalStaffAppointmentService.save(appointment);
        }

        return "redirect:/medical-staff-appointments";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        System.out.println("DELETING APPOINTMENT: " + id);
        medicalStaffAppointmentService.deleteById(id);
        return "redirect:/medical-staff-appointments";
    }
}

