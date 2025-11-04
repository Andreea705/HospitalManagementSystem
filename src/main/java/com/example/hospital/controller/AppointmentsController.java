package com.example.hospital.controller;
import com.example.hospital.model.Appointments;
import com.example.hospital.model.AppointmentStatus;
import com.example.hospital.service.AppointmentsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appointments")
public class AppointmentsController {

    private final AppointmentsService appointmentsService;

    public AppointmentsController(AppointmentsService appointmentsService) {
        this.appointmentsService = appointmentsService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("appointments", appointmentsService.findAll());
        return "appointments/index"; // → templates/appointments/index.html
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable String id, Model model) {
        var appointment = appointmentsService.findById(id)
                .orElse(null);
        model.addAttribute("appointment", appointment);
        return "appointments/details"; // → templates/appointments/details.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("appointment",
                new Appointments("", "", "", "", AppointmentStatus.ACTIVE.name()));
        model.addAttribute("statuses", AppointmentStatus.values());
        return "appointments/form";
    }

    @PostMapping
    public String save(@ModelAttribute Appointments appointment) {
        appointmentsService.save(appointment);
        return "redirect:/appointments";
    }

    @PostMapping("/{id}/delete")
    public String deleteById(@PathVariable String id) {
        appointmentsService.deleteById(id);
        return "redirect:/appointments";
    }

    @GetMapping("/{id}/exists")
    //@ResponseBody // răspuns text simplu (pentru testare rapidă)
    public String existsById(@PathVariable String id) {
        boolean exists = appointmentsService.existsById(id);
        return exists ? "Appointment exists " : "Appointment not found";
    }

    @GetMapping("/department/{departmentId}")
    public String findByDepartmentId(@PathVariable String departmentId, Model model) {
        model.addAttribute("appointments", appointmentsService.findByDepartmentId(departmentId));
        return "appointments/index";
    }
}



