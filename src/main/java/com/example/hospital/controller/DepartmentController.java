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

import java.util.List;

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

    // ============ READ METHODS ============

    @GetMapping
    public String getAllDepartments(@RequestParam(required = false) Long hospitalId,
                                    Model model) {
        if (hospitalId != null) {
            try {

                model.addAttribute("hospital",
                        hospitalService.getHospitalById(hospitalId));
                model.addAttribute("departments",
                        departmentService.getDepartmentsByHospitalId(hospitalId));
            } catch (RuntimeException e) {
                model.addAttribute("errorMessage", e.getMessage());
                model.addAttribute("departments", departmentService.getAllDepartments());
            }
        } else {
            model.addAttribute("departments", departmentService.getAllDepartments());
        }

        model.addAttribute("hospitals", hospitalService.getAllHospitals());
        return "departments/index";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long hospitalId,
                                 Model model) {
        Department department = new Department();
        model.addAttribute("department", department);
        model.addAttribute("hospitals", hospitalService.getAllHospitals());
        model.addAttribute("selectedHospitalId", hospitalId);
        return "departments/form";
    }

    @GetMapping("/{id}")
    public String getDepartmentById(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Department department = departmentService.getDepartmentById(id);
            model.addAttribute("department", department);
            return "departments/details";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/departments";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Department department = departmentService.getDepartmentById(id);
            model.addAttribute("department", department);
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "departments/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/departments";
        }
    }


    // ============ CREATE DEPARTMENT ============

    @PostMapping
    public String createDepartment(@Valid @ModelAttribute Department department,
                                   BindingResult bindingResult,
                                   @RequestParam Long hospitalId,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            model.addAttribute("selectedHospitalId", hospitalId);
            return "departments/form";
        }

        try {
            departmentService.createDepartment(department, hospitalId);
            redirectAttributes.addFlashAttribute("successMessage", "Department created successfully.");
            return "redirect:/departments?hospitalId=" + hospitalId;
        } catch (RuntimeException e) {

            if (e.getMessage().contains("Hospital not found") || e.getMessage().contains("Hospital ID must be provided")) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                return "redirect:/departments";
            }

            handleServiceError(e, bindingResult, model, hospitalId);
            return "departments/form";
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
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "departments/form";
        }

        try {

            departmentService.updateDepartment(id, department, hospitalId);
            redirectAttributes.addFlashAttribute("successMessage", "Department updated successfully.");

            if (hospitalId != null) {
                return "redirect:/departments?hospitalId=" + hospitalId;
            }
            return "redirect:/departments";
        } catch (RuntimeException e) {

            if (e.getMessage().contains("Hospital not found") || e.getMessage().contains("Hospital ID must be provided")) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                return "redirect:/departments";
            }

            handleServiceError(e, bindingResult, model, hospitalId);
            return "departments/form";
        }
    }



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


    @PostMapping("/{id}/increase-rooms")
    public String increaseRoomNumbers(@PathVariable Long id,
                                      @RequestParam int additionalRooms,
                                      @RequestParam(required = false) Long hospitalId,
                                      RedirectAttributes redirectAttributes) {
        try {
            departmentService.increaseRoomNumbers(id, additionalRooms);
            redirectAttributes.addFlashAttribute("successMessage", "Room count increased.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return getRedirectPath(hospitalId);
    }

    @PostMapping("/{id}/decrease-rooms")
    public String decreaseRoomNumbers(@PathVariable Long id,
                                      @RequestParam int roomsToRemove,
                                      @RequestParam(required = false) Long hospitalId,
                                      RedirectAttributes redirectAttributes) {
        try {
            departmentService.decreaseRoomNumbers(id, roomsToRemove);
            redirectAttributes.addFlashAttribute("successMessage", "Room count decreased.");
        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return getRedirectPath(hospitalId);
    }

    @PostMapping("/{id}/assign-head")
    public String assignHead(@PathVariable Long id,
                             @RequestParam String newHead,
                             @RequestParam(required = false) Long hospitalId,
                             RedirectAttributes redirectAttributes) {
        try {
            departmentService.assignHead(id, newHead);
            redirectAttributes.addFlashAttribute("successMessage", "Department head assigned.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return getRedirectPath(hospitalId);
    }

    @GetMapping("/search")
    public String searchDepartments(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) String departmentHead,
                                    @RequestParam(required = false) Long hospitalId,
                                    @RequestParam(required = false) Boolean hasCapacity,
                                    Model model) {

        List<Department> departments = departmentService.findDepartmentsByCriteria(
                name, departmentHead, hospitalId, hasCapacity);

        model.addAttribute("departments", departments);
        model.addAttribute("hospitals", hospitalService.getAllHospitals());
        model.addAttribute("searchName", name);
        model.addAttribute("searchHead", departmentHead);
        model.addAttribute("searchHospitalId", hospitalId);
        model.addAttribute("searchHasCapacity", hasCapacity);

        return "departments/index";
    }


    private String getRedirectPath(Long hospitalId) {
        if (hospitalId != null) {
            return "redirect:/departments?hospitalId=" + hospitalId;
        }
        return "redirect:/departments";
    }


    private void handleServiceError(RuntimeException e, BindingResult bindingResult, Model model, Long hospitalId) {
        String message = e.getMessage();

        if (message.contains("already exists")) {
            bindingResult.rejectValue("name", "error.department.uniqueness", message);
        } else if (message.contains("Room numbers cannot be negative")) {
            bindingResult.rejectValue("roomNumbers", "error.department.roomnumbers", message);
        } else if (message.contains("Rooms to remove must be positive") || message.contains("Cannot remove")) {
            bindingResult.rejectValue("roomNumbers", "error.department.roomlogic", message);
        } else {

            model.addAttribute("generalError", "Operation failed: " + message);
        }

        model.addAttribute("hospitals", hospitalService.getAllHospitals());
        model.addAttribute("selectedHospitalId", hospitalId);
    }
}