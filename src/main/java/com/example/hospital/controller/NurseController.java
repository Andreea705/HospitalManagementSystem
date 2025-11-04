package com.example.hospital.controller;

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

    @PostMapping
    public String save(@ModelAttribute Nurse nurse) {
        nurseService.save(nurse);
        return "redirect:/nurses"; // Corectat - redirect corect
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Nurse nurse = nurseService.findById(id).orElse(null);
        if (nurse == null) {
            return "redirect:/nurses";
        }
        model.addAttribute("nurse", nurse);
        return "nurses/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable String id, @ModelAttribute Nurse nurse) {
        nurse.setId(id); // Asigură că ID-ul este setat corect
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
}
