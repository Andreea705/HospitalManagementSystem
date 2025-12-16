package com.example.hospital.controller;

import com.example.hospital.model.Department;
import com.example.hospital.service.DepartmentService;
import com.example.hospital.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final HospitalService hospitalService;

    public DepartmentController(DepartmentService departmentService,
                                HospitalService hospitalService) {
        this.departmentService = departmentService;
        this.hospitalService = hospitalService;
    }

    // ============ 1. LISTE MIT SORTIERUNG & FILTERUNG [cite: 24, 106] ============
    @GetMapping
    public String getAllDepartments(
            @RequestParam(required = false) Long hospitalId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String departmentHead,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        /* Die Filterung und Sortierung erfolgt vollständig im Backend [cite: 41, 66] */
        model.addAttribute("departments", departmentService.getFilteredAndSortedDepartments(
                hospitalId, name, departmentHead, sortField, sortDir));

        // Auswahlfelder für Krankenhäuser laden (Fix: 4 Argumente für den Service)
        model.addAttribute("hospitals", hospitalService.getAllHospitals(null, null, "name", "asc"));

        // UI-Zustand beibehalten (Werte bleiben nach Filtern im Formular) [cite: 74, 126]
        model.addAttribute("selectedHospitalId", hospitalId);
        model.addAttribute("name", name);
        model.addAttribute("departmentHead", departmentHead);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "departments/index";
    }

    // ============ 2. CREATE / ERSTELLEN [cite: 106] ============
    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long hospitalId, Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("hospitals", hospitalService.getAllHospitals(null, null, "name", "asc"));
        model.addAttribute("selectedHospitalId", hospitalId);
        return "departments/form";
    }

    @PostMapping
    public String createDepartment(@Valid @ModelAttribute Department department,
                                   BindingResult bindingResult,
                                   @RequestParam Long hospitalId,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("hospitals", hospitalService.getAllHospitals(null, null, "name", "asc"));
            return "departments/form";
        }

        try {
            departmentService.createDepartment(department, hospitalId);
            redirectAttributes.addFlashAttribute("successMessage", "Department created successfully.");
            return "redirect:/departments?hospitalId=" + hospitalId;
        } catch (RuntimeException e) {
            handleServiceError(e, bindingResult, model, hospitalId);
            return "departments/form";
        }
    }

    // ============ 3. READ / DETAILS ============
    @GetMapping("/{id}")
    public String getDepartmentById(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("department", departmentService.getDepartmentById(id));
            return "departments/details";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/departments";
        }
    }

    // ============ 4. UPDATE / BEARBEITEN [cite: 106] ============
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("department", departmentService.getDepartmentById(id));
            model.addAttribute("hospitals", hospitalService.getAllHospitals(null, null, "name", "asc"));
            return "departments/form";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/departments";
        }
    }

    @PostMapping("/update/{id}")
    public String updateDepartment(@PathVariable Long id,
                                   @Valid @ModelAttribute Department department,
                                   BindingResult bindingResult,
                                   @RequestParam(required = false) Long hospitalId,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("hospitals", hospitalService.getAllHospitals(null, null, "name", "asc"));
            return "departments/form";
        }

        try {
            departmentService.updateDepartment(id, department, hospitalId);
            redirectAttributes.addFlashAttribute("successMessage", "Department updated successfully.");
            return getRedirectPath(hospitalId);
        } catch (RuntimeException e) {
            handleServiceError(e, bindingResult, model, hospitalId);
            return "departments/form";
        }
    }

    // ============ 5. DELETE / LÖSCHEN [cite: 106] ============
    @PostMapping("/{id}/delete")
    public String deleteDepartment(@PathVariable Long id,
                                   @RequestParam(required = false) Long hospitalId,
                                   RedirectAttributes redirectAttributes) {
        try {
            departmentService.deleteDepartment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Department deleted successfully.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return getRedirectPath(hospitalId);
    }

    // ============ 6. BUSINESS LOGIC (Zimmerverwaltung) [cite: 79, 119] ============
    @PostMapping("/{id}/increase-rooms")
    public String increaseRoomNumbers(@PathVariable Long id,
                                      @RequestParam int additionalRooms,
                                      @RequestParam(required = false) Long hospitalId,
                                      RedirectAttributes ra) {
        try {
            departmentService.increaseRoomNumbers(id, additionalRooms);
            ra.addFlashAttribute("successMessage", "Room count increased.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return getRedirectPath(hospitalId);
    }

    @PostMapping("/{id}/decrease-rooms")
    public String decreaseRoomNumbers(@PathVariable Long id,
                                      @RequestParam int roomsToRemove,
                                      @RequestParam(required = false) Long hospitalId,
                                      RedirectAttributes ra) {
        try {
            departmentService.decreaseRoomNumbers(id, roomsToRemove);
            ra.addFlashAttribute("successMessage", "Room count decreased.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return getRedirectPath(hospitalId);
    }

    // ============ HILFSMETHODEN ============
    private String getRedirectPath(Long hospitalId) {
        return hospitalId != null ? "redirect:/departments?hospitalId=" + hospitalId : "redirect:/departments";
    }

    private void handleServiceError(RuntimeException e, BindingResult bindingResult, Model model, Long hospitalId) {
        String message = e.getMessage();
        if (message.contains("already exists")) {
            bindingResult.rejectValue("name", "error.department.uniqueness", message);
        } else if (message.contains("Room numbers")) {
            bindingResult.rejectValue("roomNumbers", "error.department.roomnumbers", message);
        } else {
            model.addAttribute("generalError", "Operation failed: " + message);
        }
        model.addAttribute("hospitals", hospitalService.getAllHospitals(null, null, "name", "asc"));
        model.addAttribute("selectedHospitalId", hospitalId);
    }
}