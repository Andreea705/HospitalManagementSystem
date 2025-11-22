package com.example.hospital.controller;

import com.example.hospital.model.Hospital;
import com.example.hospital.model.Nurse;
import com.example.hospital.service.NurseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/nurses")
public class NurseController {

    private final NurseService nurseService;

    public NurseController(NurseService nurseService) {
        this.nurseService = nurseService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("nurses", nurseService.findAll());
        return "nurses/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("nurse", new Nurse());
        return "nurses/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        java.util.Optional<Nurse> nurse = nurseService.findById(id);
        if (nurse.isPresent()) {
            model.addAttribute("nurse", nurse.get());
            return "nurses/form";
        } else {
            return "redirect:/nurses";
        }
    }

    @GetMapping("/{id}")
    public String showDetails(@PathVariable String id, Model model) {
        java.util.Optional<Nurse> nurse = nurseService.findById(id);
        if (nurse.isPresent()) {
            model.addAttribute("nurse", nurse.get());
            return "nurses/details"; // Aceasta va fi pagina ta de details
        } else {
            return "redirect:/nurses";
        }
    }

    @PostMapping
    public String save(@ModelAttribute Nurse nurse) {
        nurseService.save(nurse);
        return "redirect:/nurses";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        nurseService.deleteById(id);
        return "redirect:/nurses";
    }

    @GetMapping("/{id}/exists")
    @ResponseBody
    public String existsById(@PathVariable String id) {
        boolean exists = nurseService.existsById(id);
        return exists ? "Nurse exists" : "Nurse not found";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable String id, @ModelAttribute Nurse nurse) {
        nurseService.updateNurse(id, nurse);
        return "redirect:/nurses";
    }
}
