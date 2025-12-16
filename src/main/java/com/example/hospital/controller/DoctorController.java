package com.example.hospital.controller;

import com.example.hospital.model.Doctor;
import com.example.hospital.service.DoctorService;
import com.example.hospital.service.DepartmentService;
import com.example.hospital.service.AppointmentsService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final DepartmentService departmentService;
    private final AppointmentsService appointmentService;

    public DoctorController(DoctorService doctorService,
                            DepartmentService departmentService,
                            AppointmentsService appointmentService) {
        this.doctorService = doctorService;
        this.departmentService = departmentService;
        this.appointmentService = appointmentService;
    }

    // ============ LIST ALL DOCTORS ============

//    @GetMapping
//    public String getAllDoctors(@RequestParam(required = false) String name,
//                                @RequestParam(required = false) String specialization,
//                                @RequestParam(required = false) Long departmentId,
//                                Model model) {
//
//        List<Doctor> doctors;
//
//        if (name != null || specialization != null || departmentId != null) {
//            String departmentIdStr = departmentId != null ? departmentId.toString() : null;
//            doctors = doctorService.searchDoctors(name, specialization, departmentIdStr);
//        } else {
//            doctors = doctorService.getAllDoctors();
//        }
//
//        model.addAttribute("doctors", doctors);
//        model.addAttribute("departments", departmentService.getAllDepartments());
//
//        model.addAttribute("searchName", name);
//        model.addAttribute("searchSpecialization", specialization);
//        model.addAttribute("searchDepartmentId", departmentId);
//
//        // Statistici
//        model.addAttribute("totalDoctors", doctorService.countAllDoctors());
//
//        return "doctors/index";
//    }
    @GetMapping
    public String getAllDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "medicalStaffName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        List<Doctor> doctors = doctorService.searchDoctors(
                name,
                specialization,
                departmentId,
                sortBy,
                sortDir
        );

        model.addAttribute("doctors", doctors);
        model.addAttribute("departments", departmentService.getAllDepartments());

        // pastreaza filtrele
        model.addAttribute("searchName", name);
        model.addAttribute("searchSpecialization", specialization);
        model.addAttribute("searchDepartmentId", departmentId);

        // sortare
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir",
                sortDir.equals("asc") ? "desc" : "asc");

        return "doctors/index";
    }


    // ============ SHOW CREATE FORM ============

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long departmentId,
                                 Model model) {
        Doctor doctor = new Doctor();
        doctor.setMedicalStaffId("DOC" + System.currentTimeMillis()); // ID unic

        model.addAttribute("doctor", doctor);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("selectedDepartmentId", departmentId);

        return "doctors/form";
    }

    // ============ VIEW DOCTOR DETAILS ============

    @GetMapping("/{id}")
    public String getDoctorById(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id);

        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointmentService.getAppointmentsByDoctor(id));

        return "doctors/details";
    }

    // ============ CREATE DOCTOR ============

    @PostMapping
    public String createDoctor(@Valid @ModelAttribute Doctor doctor,
                               BindingResult bindingResult,
                               @RequestParam(required = false) Long departmentId,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "doctors/form";
        }

        try {
            Doctor createdDoctor = doctorService.createDoctor(doctor, departmentId);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Doctor " + createdDoctor.getMedicalStaffName() + " created successfully");

            return "redirect:/doctors";

        } catch (RuntimeException e) {
            bindingResult.rejectValue("medicalStaffId", "error.doctor", e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "doctors/form";
        }
    }

    // ============ SHOW EDIT FORM ============

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id);

        model.addAttribute("doctor", doctor);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("isEdit", true);

        return "doctors/form";
    }

    // ============ UPDATE DOCTOR ============

    @PostMapping("/update/{id}")
    public String updateDoctor(@PathVariable Long id,
                               @Valid @ModelAttribute Doctor doctor,
                               BindingResult bindingResult,
                               @RequestParam(required = false) Long departmentId,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("isEdit", true);
            return "doctors/form";
        }

        try {
            Doctor updatedDoctor = doctorService.updateDoctor(id, doctor, departmentId);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Doctor " + updatedDoctor.getMedicalStaffName() + " updated successfully");

            return "redirect:/doctors";

        } catch (RuntimeException e) {
            bindingResult.rejectValue("medicalStaffId", "error.doctor", e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("isEdit", true);
            return "doctors/form";
        }
    }

    // ============ DELETE DOCTOR ============

    @PostMapping("/{id}/delete")
    public String deleteDoctor(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {

        try {
            Doctor doctor = doctorService.getDoctorById(id);
            doctorService.deleteDoctor(id);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Doctor " + doctor.getMedicalStaffName() + " deleted successfully");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/doctors";
    }

    // ============ BUSINESS OPERATIONS ============

    @PostMapping("/{id}/assign-department")
    public String assignToDepartment(@PathVariable Long id,
                                     @RequestParam Long departmentId,
                                     RedirectAttributes redirectAttributes) {
        try {
            doctorService.assignToDepartment(id, departmentId);
            redirectAttributes.addFlashAttribute("successMessage", "Doctor assigned to department");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/doctors/" + id;
    }

    @PostMapping("/{id}/remove-department")
    public String removeFromDepartment(@PathVariable Long id,
                                       RedirectAttributes redirectAttributes) {
        try {
            doctorService.assignToDepartment(id, null);
            redirectAttributes.addFlashAttribute("successMessage", "Doctor removed from department");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/doctors/" + id;
    }

    // ============ SPECIAL VIEWS ============

    @GetMapping("/without-department")
    public String getDoctorsWithoutDepartment(Model model) {
        model.addAttribute("doctors", doctorService.getDoctorsWithoutDepartment());
        model.addAttribute("viewTitle", "Doctors Without Department");
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "doctors/index";
    }

    @GetMapping("/by-specialization")
    public String getDoctorsBySpecialization(@RequestParam String specialization,
                                             Model model) {
        model.addAttribute("doctors", doctorService.getDoctorsBySpecialization(specialization));
        model.addAttribute("viewTitle", "Doctors with Specialization: " + specialization);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("searchSpecialization", specialization);
        return "doctors/index";
    }

    @GetMapping("/department/{departmentId}")
    public String getDoctorsByDepartment(@PathVariable Long departmentId, Model model) {
        model.addAttribute("doctors", doctorService.getDoctorsByDepartment(departmentId));
        model.addAttribute("viewTitle", "Doctors in Department");
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("selectedDepartment", departmentService.getDepartmentById(departmentId));

        return "doctors/index";
    }
}