package com.example.hospital.controller;

import com.example.hospital.model.Hospital;
import com.example.hospital.service.HospitalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("hospitals", hospitalService.getAllHospitals());
        return "hospitals/index";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable String id, Model model) {
        Hospital hospital = hospitalService.getHospitalById(id);
        model.addAttribute("hospital", hospital);
        return "hospitals/details"; // → templates/hospitals/details.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("hospital", new Hospital());
        return "hospitals/form";
    }

    @PostMapping
    public String save(@ModelAttribute Hospital hospital) {
        hospitalService.createHospital(hospital);
        return "redirect:/hospitals";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Hospital hospital = hospitalService.getHospitalById(id);
        model.addAttribute("hospital", hospital);
        return "hospitals/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable String id, @ModelAttribute Hospital hospital) {
        hospitalService.updateHospital(id, hospital);
        return "redirect:/hospitals";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        hospitalService.deleteHospital(id);
        return "redirect:/hospitals";
    }

    @GetMapping("/{id}/exists")
    public String existsById(@PathVariable String id, Model model) {
        boolean exists = hospitalService.getHospitalById(id) != null;
        model.addAttribute("existsMessage", exists ? "Hospital exists" : "Hospital not found");
        return "hospitals/exists";
    }
}


