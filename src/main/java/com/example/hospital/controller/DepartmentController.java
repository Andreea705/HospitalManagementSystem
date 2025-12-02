package com.example.hospital.controller;

import com.example.hospital.model.Department;
import com.example.hospital.service.DepartmentService;
import com.example.hospital.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public String getAllDepartments(@RequestParam(required = false) Long hospitalId,
                                    Model model) {
        if (hospitalId != null) {
            model.addAttribute("departments",
                    departmentService.getDepartmentsByHospitalId(hospitalId));
            model.addAttribute("hospital",
                    hospitalService.getHospitalById(hospitalId));
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
    public String getDepartmentById(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id);
        model.addAttribute("department", department);
        return "departments/details";
    }


    @PostMapping
    public String createDepartment(@Valid @ModelAttribute Department department,
                                   BindingResult bindingResult,
                                   @RequestParam Long hospitalId,
                                   Model model) {


        if (bindingResult.hasErrors()) {
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "departments/form";
        }

        if (departmentService.departmentNameExistsInHospital(
                department.getName(), hospitalId)) {
            bindingResult.rejectValue("name", "error.department",
                    "A department with this name already exists in this hospital");
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "departments/form";
        }


        if (department.getRoomNumbers() < 0) {
            bindingResult.rejectValue("roomNumbers", "error.department",
                    "Room numbers cannot be negative");
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "departments/form";
        }

        try {
            departmentService.createDepartment(department, hospitalId);
            return "redirect:/departments?hospitalId=" + hospitalId;
        } catch (RuntimeException e) {
            bindingResult.reject("error.department", e.getMessage());
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "departments/form";
        }
    }



    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id);
        model.addAttribute("department", department);
        model.addAttribute("hospitals", hospitalService.getAllHospitals());
        return "departments/form";
    }


    @PostMapping("/update/{id}")
    public String updateDepartment(@PathVariable Long id,
                                   @Valid @ModelAttribute Department department,
                                   BindingResult bindingResult,
                                   @RequestParam(required = false) Long hospitalId,
                                   Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "departments/form";
        }

        if (department.getRoomNumbers() < 0) {
            bindingResult.rejectValue("roomNumbers", "error.department",
                    "Room numbers cannot be negative");
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "departments/form";
        }

        try {
            departmentService.updateDepartment(id, department, hospitalId);

            if (hospitalId != null) {
                return "redirect:/departments?hospitalId=" + hospitalId;
            }
            return "redirect:/departments";
        } catch (RuntimeException e) {

            bindingResult.reject("error.department", e.getMessage());
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "departments/form";
        }
    }


    @PostMapping("/{id}/delete")
    public String deleteDepartment(@PathVariable Long id,
                                   @RequestParam(required = false) Long hospitalId) {
        departmentService.deleteDepartment(id);

        if (hospitalId != null) {
            return "redirect:/departments?hospitalId=" + hospitalId;
        }
        return "redirect:/departments";
    }


    @PostMapping("/{id}/increase-rooms")
    public String increaseRoomNumbers(@PathVariable Long id,
                                      @RequestParam int additionalRooms,
                                      @RequestParam(required = false) Long hospitalId) {
        try {
            departmentService.increaseRoomNumbers(id, additionalRooms);
        } catch (RuntimeException e) {
        }

        if (hospitalId != null) {
            return "redirect:/departments?hospitalId=" + hospitalId;
        }
        return "redirect:/departments";
    }

    @PostMapping("/{id}/decrease-rooms")
    public String decreaseRoomNumbers(@PathVariable Long id,
                                      @RequestParam int roomsToRemove,
                                      @RequestParam(required = false) Long hospitalId) {
        try {
            departmentService.decreaseRoomNumbers(id, roomsToRemove);
        } catch (RuntimeException e) {

        }

        if (hospitalId != null) {
            return "redirect:/departments?hospitalId=" + hospitalId;
        }
        return "redirect:/departments";
    }

    @PostMapping("/{id}/assign-head")
    public String assignHead(@PathVariable Long id,
                             @RequestParam String newHead,
                             @RequestParam(required = false) Long hospitalId) {
        departmentService.assignHead(id, newHead);

        if (hospitalId != null) {
            return "redirect:/departments?hospitalId=" + hospitalId;
        }
        return "redirect:/departments";
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
}
