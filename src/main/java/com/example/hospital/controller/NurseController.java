package com.example.hospital.controller;

import com.example.hospital.model.Department;
import com.example.hospital.model.Nurse;
import com.example.hospital.model.QualificationLevel;
import com.example.hospital.service.NurseService;
import com.example.hospital.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/nurses")
public class NurseController {

    private final NurseService nurseService;
    private final DepartmentService departmentService;

    public NurseController(NurseService nurseService, DepartmentService departmentService) {
        this.nurseService = nurseService;
        this.departmentService = departmentService;
    }

    // ============ LIST ALL NURSES ============
    @GetMapping
    public String getAllNurses(@RequestParam(required = false) Long departmentId,
                               Model model) {
        List<Nurse> nurses;

        if (departmentId != null) {
            // Show nurses for specific department
            nurses = nurseService.getNursesByDepartment(departmentId);
            model.addAttribute("department",
                    departmentService.getDepartmentById(departmentId));
        } else {
            // Show all nurses
            nurses = nurseService.getAllNurses();
        }

        model.addAttribute("nurses", nurses);
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "nurses/index";
    }

    // ============ SHOW CREATE FORM ============
    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long departmentId,
                                 Model model) {
        Nurse nurse = new Nurse();
        model.addAttribute("nurse", nurse);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("qualificationLevels", QualificationLevel.values());
        model.addAttribute("shifts", List.of("Day", "Night", "Evening", "Weekend"));
        model.addAttribute("selectedDepartmentId", departmentId);
        return "nurses/form";
    }

    // ============ VIEW NURSE DETAILS ============
    @GetMapping("/{id}")
    public String getNurseById(@PathVariable Long id, Model model) {
        Nurse nurse = nurseService.getNurseById(id);
        model.addAttribute("nurse", nurse);
        return "nurses/details";
    }

    // ============ CREATE NURSE ============
    @PostMapping
    public String createNurse(@Valid @ModelAttribute Nurse nurse,
                              BindingResult bindingResult,
                              @RequestParam(required = false) Long departmentId,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        // Basic validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("qualificationLevels", QualificationLevel.values());
            model.addAttribute("shifts", List.of("Day", "Night", "Evening", "Weekend"));
            return "nurses/form";
        }

        // Business validation: Check nurse code uniqueness
        if (nurseService.nurseExistsByMedicalStaffId(nurse.getMedicalStaffId())) {
            bindingResult.rejectValue("medicalStaffId", "error.nurse",
                    "A nurse with this code already exists");
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("qualificationLevels", QualificationLevel.values());
            model.addAttribute("shifts", List.of("Day", "Night", "Evening", "Weekend"));
            return "nurses/form";
        }

        try {
            nurseService.createNurse(nurse, departmentId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Nurse created successfully!");
            return "redirect:/nurses?departmentId=" + departmentId;

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("qualificationLevels", QualificationLevel.values());
            model.addAttribute("shifts", List.of("Day", "Night", "Evening", "Weekend"));
            return "nurses/form";
        }
    }

    // ============ SHOW EDIT FORM ============
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Nurse nurse = nurseService.getNurseById(id);
        model.addAttribute("nurse", nurse);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("qualificationLevels", QualificationLevel.values());
        model.addAttribute("shifts", List.of("Day", "Night", "Evening", "Weekend"));
        return "nurses/form";
    }

    // ============ UPDATE NURSE ============
    @PostMapping("/update/{id}")
    public String updateNurse(@PathVariable Long id,
                              @Valid @ModelAttribute Nurse nurse,
                              BindingResult bindingResult,
                              @RequestParam(required = false) Long departmentId,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("qualificationLevels", QualificationLevel.values());
            model.addAttribute("shifts", List.of("Day", "Night", "Evening", "Weekend"));
            return "nurses/form";
        }

        try {
            nurseService.updateNurse(id, nurse, departmentId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Nurse updated successfully!");

            // Redirect back to department context if provided
            if (departmentId != null) {
                return "redirect:/nurses?departmentId=" + departmentId;
            }
            return "redirect:/nurses";

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            Nurse existing = nurseService.getNurseById(id);
            model.addAttribute("nurse", existing);
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("qualificationLevels", QualificationLevel.values());
            model.addAttribute("shifts", List.of("Day", "Night", "Evening", "Weekend"));
            return "nurses/form";
        }
    }

    // ============ DELETE NURSE ============
    @PostMapping("/{id}/delete")
    public String deleteNurse(@PathVariable Long id,
                              @RequestParam(required = false) Long departmentId,
                              RedirectAttributes redirectAttributes) {
        try {
            Nurse nurse = nurseService.getNurseById(id);
            nurseService.deleteNurse(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Nurse '" + nurse.getMedicalStaffName() + "' deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (departmentId != null) {
            return "redirect:/nurses?departmentId=" + departmentId;
        }
        return "redirect:/nurses";
    }

    // ============ BUSINESS OPERATIONS ============
    @PostMapping("/{id}/toggle-duty")
    public String toggleDutyStatus(@PathVariable Long id,
                                   @RequestParam(required = false) Long departmentId,
                                   RedirectAttributes redirectAttributes) {
        try {
            Nurse updated = nurseService.toggleDutyStatus(id);
            String status = updated.isOnDuty() ? "on duty" : "off duty";
            redirectAttributes.addFlashAttribute("successMessage",
                    "Nurse '" + updated.getMedicalStaffName() + "' is now " + status);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (departmentId != null) {
            return "redirect:/nurses?departmentId=" + departmentId;
        }
        return "redirect:/nurses";
    }

    @PostMapping("/{id}/change-shift")
    public String changeShift(@PathVariable Long id,
                              @RequestParam String newShift,
                              @RequestParam(required = false) Long departmentId,
                              RedirectAttributes redirectAttributes) {
        try {
            Nurse updated = nurseService.changeShift(id, newShift);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Nurse '" + updated.getMedicalStaffName() + "' shift changed to " + newShift);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (departmentId != null) {
            return "redirect:/nurses?departmentId=" + departmentId;
        }
        return "redirect:/nurses";
    }

    @PostMapping("/{id}/promote")
    public String promoteNurse(@PathVariable Long id,
                               @RequestParam QualificationLevel newQualification,
                               @RequestParam(required = false) Long departmentId,
                               RedirectAttributes redirectAttributes) {
        try {
            Nurse updated = nurseService.promoteNurse(id, newQualification);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Nurse '" + updated.getMedicalStaffName() + "' promoted to " + newQualification);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (departmentId != null) {
            return "redirect:/nurses?departmentId=" + departmentId;
        }
        return "redirect:/nurses";
    }

    @PostMapping("/{id}/assign-department")
    public String assignToDepartment(@PathVariable Long id,
                                     @RequestParam Long newDepartmentId,
                                     @RequestParam(required = false) Long currentDepartmentId,
                                     RedirectAttributes redirectAttributes) {
        try {
            Nurse updated = nurseService.assignToDepartment(id, newDepartmentId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Nurse '" + updated.getMedicalStaffName() + "' assigned to new department");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (currentDepartmentId != null) {
            return "redirect:/nurses?departmentId=" + currentDepartmentId;
        }
        return "redirect:/nurses";
    }

    // ============ SEARCH/FILTER ============
    @GetMapping("/search")
    public String searchNurses(@RequestParam(required = false) String name,
                               @RequestParam(required = false) Long departmentId,
                               @RequestParam(required = false) QualificationLevel qualification,
                               @RequestParam(required = false) String shift,
                               @RequestParam(required = false) Boolean onDuty,
                               Model model) {

        List<Nurse> nurses = nurseService.searchNurses(name, departmentId, qualification, shift, onDuty);

        model.addAttribute("nurses", nurses);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("qualificationLevels", QualificationLevel.values());
        model.addAttribute("searchName", name);
        model.addAttribute("searchDepartmentId", departmentId);
        model.addAttribute("searchQualification", qualification);
        model.addAttribute("searchShift", shift);
        model.addAttribute("searchOnDuty", onDuty);

        return "nurses/index";
    }
}
