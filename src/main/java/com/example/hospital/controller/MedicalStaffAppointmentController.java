//package com.example.hospital.controller;
//
//import com.example.hospital.model.MedicalStaffAppointment;
//import com.example.hospital.service.MedicalStaffAppointmentService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/medical-staff-appointments")
//public class MedicalStaffAppointmentController {
//
//    private final MedicalStaffAppointmentService medicalStaffAppointmentService;
//
//    public MedicalStaffAppointmentController(MedicalStaffAppointmentService medicalStaffAppointmentService) {
//        this.medicalStaffAppointmentService = medicalStaffAppointmentService;
//    }
//
//    @GetMapping
//    public String findAll(Model model) {
//        model.addAttribute("medicalStaffAppointments", medicalStaffAppointmentService.findAll());
//        return "medicalStaffAppointment/index";
//    }
//
//    @GetMapping("/new")
//    public String showCreateForm(Model model) {
//        model.addAttribute("appointment", new MedicalStaffAppointment());
//        return "medicalStaffAppointment/form";
//    }
//
//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable String id, Model model) {
//        Optional<MedicalStaffAppointment> appointment = medicalStaffAppointmentService.findById(id);
//        if (appointment.isPresent()) {
//            model.addAttribute("appointment", appointment.get());
//            return "medicalStaffAppointment/form";
//        } else return "redirect:/medical-staff-appointments";
//    }
//
//    @GetMapping("/{id}")
//    public String showDetails(@PathVariable String id, Model model) {
//        Optional<MedicalStaffAppointment> appointment = medicalStaffAppointmentService.findById(id);
//        if (appointment.isPresent()) {
//            model.addAttribute("appointment", appointment.get());
//            return "medicalStaffAppointment/details";
//        } else return "redirect:/medical-staff-appointments";
//    }
//
//    @PostMapping("/save")
//    public String saveOrUpdate(@ModelAttribute MedicalStaffAppointment appointment) {
//        if (appointment.getMedicalStaffAppointmentId() != null && !appointment.getMedicalStaffAppointmentId().isEmpty()) {
//            // UPDATE  appointment
//            medicalStaffAppointmentService.updateMedicalStaffAppointment(appointment.getMedicalStaffAppointmentId(), appointment);
//        } else
//            // CREATE new appointment
//            medicalStaffAppointmentService.save(appointment);
//        return "redirect:/medical-staff-appointments";
//    }
//
//    @PostMapping("/{id}/delete")
//    public String delete(@PathVariable String id) {
//        medicalStaffAppointmentService.deleteById(id);
//        return "redirect:/medical-staff-appointments";
//    }
//}
//
