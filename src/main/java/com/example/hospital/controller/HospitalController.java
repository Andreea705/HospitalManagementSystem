package com.example.hospital.controller;

import com.example.hospital.model.Hospital;
import com.example.hospital.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    // ============ LIST ALL HOSPITALS ============
    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("hospitals", hospitalService.getAllHospitals());
        return "hospitals/index";
    }

    // ============ SHOW CREATE FORM ============
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("hospital", new Hospital());
        return "hospitals/form";
    }

    // ============ VIEW DETAILS ============
    @GetMapping("/{id}")
    public String viewDetails(@PathVariable Long id, Model model) {  // Long statt String!
        Hospital hospital = hospitalService.getHospitalById(id);
        model.addAttribute("hospital", hospital);
        return "hospitals/details";
    }

    // ============ CREATE HOSPITAL ============
    @PostMapping
    public String save(@Valid @ModelAttribute Hospital hospital,  // @Valid hinzufügen!
                       BindingResult bindingResult,  // Für Validierung
                       Model model) {

        // Prüfe Validierung
        if (bindingResult.hasErrors()) {
            return "hospitals/form";  // Zurück zum Formular mit Fehlern
        }

        // Prüfe ob Hospital mit diesem Namen bereits existiert
        if (hospitalService.hospitalExistsByName(hospital.getName())) {
            bindingResult.rejectValue("name", "error.hospital",
                    "A hospital with this name already exists");
            return "hospitals/form";
        }

        hospitalService.createHospital(hospital);
        return "redirect:/hospitals";
    }

    // ============ SHOW EDIT FORM ============
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {  // Long statt String!
        Hospital hospital = hospitalService.getHospitalById(id);
        model.addAttribute("hospital", hospital);
        return "hospitals/form";
    }

    // ============ UPDATE HOSPITAL ============
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,  // Long statt String!
                         @Valid @ModelAttribute Hospital hospital,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "hospitals/form";
        }

        // Prüfe ob ein ANDERES Hospital mit diesem Namen existiert
        Hospital existingHospital = hospitalService.getHospitalById(id);
        if (!existingHospital.getName().equals(hospital.getName()) &&
                hospitalService.hospitalExistsByName(hospital.getName())) {
            bindingResult.rejectValue("name", "error.hospital",
                    "A hospital with this name already exists");
            return "hospitals/form";
        }

        hospitalService.updateHospital(id, hospital);
        return "redirect:/hospitals";
    }

    // ============ DELETE HOSPITAL ============
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {  // Long statt String!
        hospitalService.deleteHospital(id);
        return "redirect:/hospitals";
    }
}


