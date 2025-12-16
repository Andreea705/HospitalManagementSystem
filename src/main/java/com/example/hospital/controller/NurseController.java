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

    // ============ LIST ALL NURSES (UNIFIED METHOD) ============
    @GetMapping
    public String getAllNurses(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir,
            Model model) {

        List<Nurse> nurses;

        // Folosim metoda de search cu toți parametrii
        nurses = nurseService.searchNurses(
                null,           // name
                departmentId,   // departmentId
                null,           // qualification
                null,           // shift
                null,           // onDuty
                sortBy,         // sortBy
                sortDir         // sortDir
        );

        if (departmentId != null) {
            model.addAttribute("department", departmentService.getDepartmentById(departmentId));
        }

        model.addAttribute("nurses", nurses);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("qualificationLevels", QualificationLevel.values());

        // Păstrăm parametrii pentru view
        model.addAttribute("searchDepartmentId", departmentId);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        // Calculate reverse sort direction
        String reverseSortDir = "asc".equalsIgnoreCase(sortDir) ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortDir);

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

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("qualificationLevels", QualificationLevel.values());
            model.addAttribute("shifts", List.of("Day", "Night", "Evening", "Weekend"));
            return "nurses/form";
        }

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
    public String searchNurses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) QualificationLevel qualification,
            @RequestParam(required = false) String shift,
            @RequestParam(required = false) Boolean onDuty,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir,
            Model model) {

        System.out.println("DEBUG - Search params:");
        System.out.println("  name: " + name);
        System.out.println("  departmentId: " + departmentId);
        System.out.println("  qualification: " + qualification);
        System.out.println("  shift: " + shift);
        System.out.println("  onDuty: " + onDuty);
        System.out.println("  sortBy: " + sortBy);
        System.out.println("  sortDir: " + sortDir);

        List<Nurse> nurses = nurseService.searchNurses(
                name,
                departmentId,
                qualification,
                shift,
                onDuty,
                sortBy,
                sortDir
        );

        System.out.println("DEBUG - Found nurses: " + nurses.size());

        model.addAttribute("nurses", nurses);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("qualificationLevels", QualificationLevel.values());

        // Keep filters for form
        model.addAttribute("searchName", name);
        model.addAttribute("searchDepartmentId", departmentId);
        model.addAttribute("searchQualification", qualification);
        model.addAttribute("searchShift", shift);
        model.addAttribute("searchOnDuty", onDuty);

        // Keep sorting
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        // Calculate reverse sort direction
        String reverseSortDir = "asc".equalsIgnoreCase(sortDir) ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "nurses/index";
    }
}
