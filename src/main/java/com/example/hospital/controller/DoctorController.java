package com.example.hospital.controller;
import com.example.hospital.model.AppointmentStatus;
import com.example.hospital.model.Appointments;
import org.springframework.ui.Model;
import com.example.hospital.model.Doctor;
import com.example.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public String save(@ModelAttribute Doctor Doctor) {
        doctorService.save(Doctor);
        return "redirect:/doctor";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable String id, Model model) {
        var appointment = doctorService.findById(id)
                .orElse(null);
        model.addAttribute("doctor", doctorService.findById(id));
        return "doctor/details"; // → templates/appointments/details.html
    }

//    @GetMapping("/new")
//    public String showCreateForm(Model model) {
//        model.addAttribute("doctor", new Doctor());
//        return "doctor/form"; // → templates/doctor/form.html
//    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        return "doctor/list";
    }

    @PostMapping("/{id}/delete")
    public String deleteById(@PathVariable String id) {
        doctorService.deleteById(id);
        return "doctor/delete";
    }

    @GetMapping("/{id}/exists")
    //@ResponseBody // răspuns text simplu (pentru testare rapidă)
    public String existsById(@PathVariable String id) {
        boolean exists = doctorService.existsById(id);
        return exists ? "Docotr exists " : "Doctor not found";
    }
}

