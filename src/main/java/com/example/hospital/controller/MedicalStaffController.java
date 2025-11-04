//package com.example.hospital.controller;
//
//import com.example.hospital.model.MedicalStaff;
//import com.example.hospital.service.MedicalStaffService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@RequestMapping("/medical-staff")
//public class MedicalStaffController {
//
//    private final MedicalStaffService staffService;
//
//    public MedicalStaffController(MedicalStaffService staffService) {
//        this.staffService = staffService;
//    }
//
//    @GetMapping
//    public String findAll(Model model) {
//        model.addAttribute("staffList", staffService.findAll());
//        return "medicalStaff/index";
//    }
//
//    @GetMapping("/{id}")
//    public String findById(@PathVariable String id, Model model) {
//        MedicalStaff staff = staffService.findById(id).orElse(null);
//        model.addAttribute("staff", staff);
//        return "medicalStaff/details";
//    }
//
//    @GetMapping("/new")
//    public String showCreateForm(Model model) {
//        model.addAttribute("staff", new MedicalStaff());
//        return "medicalStaff/form";
//    }
//
//    @PostMapping
//    public String save(@ModelAttribute MedicalStaff staff) {
//        staffService.save(staff);
//        return "redirect:/medical-staff";
//    }
//
//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable String id, Model model) {
//        MedicalStaff staff = staffService.findById(id).orElse(null);
//        model.addAttribute("staff", staff);
//        return "medicalStaff/form";
//    }
//
//    @PostMapping("/update/{id}")
//    public String update(@PathVariable String id, @ModelAttribute MedicalStaff staff) {
//        staffService.save(staff);
//        return "medical-staff";
//    }
//
//    @PostMapping("/{id}/delete")
//    public String delete(@PathVariable String id) {
//        staffService.deleteById(id);
//        return "medical-staff";
//    }
//
//    @GetMapping("/{id}/exists")
//    public String existsById(@PathVariable String id, Model model) {
//        boolean exists = staffService.existsById(id);
//        model.addAttribute("existsMessage", exists ? "Medical staff exists" : "Medical staff not found");
//        return "medicalStaff/exists";
//    }
//}
