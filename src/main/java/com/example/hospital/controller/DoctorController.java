//package com.example.hospital.controller;
//
//import com.example.hospital.model.Doctor;
//import com.example.hospital.repository.DoctorRepository;
//import com.example.hospital.service.AppointmentsService;
//import com.example.hospital.service.DoctorService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/doctors")
//public class DoctorController {
//
//    private final DoctorService doctorService;
//    private final AppointmentsService appointmentsService;
//
//    @Autowired
//    public DoctorController(DoctorService doctorService, DoctorRepository doctorRepository, AppointmentsService appointmentsService) {
//        this.doctorService = doctorService;
//        this.appointmentsService = appointmentsService;
//    }
//
//    @GetMapping
//    public String getAllDoctors(Model model) {
//            model.addAttribute("doctors", doctorService.findAll());
//            return "doctors/index";
//    }
//
//    //creare formular
//    @GetMapping("/new")
//    public String showCreateForm(Model model) {
//        model.addAttribute("doctor", new Doctor());
//        return "doctors/form";
//    }
//
//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable String id, Model model) {
//        try {
//            Optional<Doctor> doctor = doctorService.findById(id);
//            if (doctor.isPresent()) {
//                model.addAttribute("doctor", doctor.get());
//                model.addAttribute("isEdit", true); // Pentru a diferenția în formular
//                return "doctors/form"; // Mereu returnează formularul de editare
//            } else {
//                return "redirect:/doctors";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "redirect:/doctors";
//        }
//    }
//
//    // ENDPOINT pt DETAILS
//    @GetMapping("/{id}")
//    public String getDoctorDetails(@PathVariable String id, Model model) {
//        try {
//            Optional<Doctor> doctor = doctorService.findById(id);
//            if (doctor.isPresent()) {
//                model.addAttribute("doctor", doctor.get());
//                return "doctors/details";
//            } else return "redirect:/doctors";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "redirect:/doctors";
//        }
//    }
//
////    @GetMapping("/{id}")
////    public String getDoctorDetails(@PathVariable String id, Model model) {
////        Optional<Doctor> doctor = doctorService.findById(id);
////        model.addAttribute("department", doctor);
////        return "doctor/details";
////    }
//
//    @PostMapping
//    public String createDoctor(@ModelAttribute Doctor doctor, Model model) {
//        try {
//            if (doctor.getRole() == null) {
//                doctor.setRole("doctor");
//            }
//
//            Doctor savedDoctor = doctorService.save(doctor);
//            System.out.println("DOCTOR SAVED SUCCESSFULLY!");
//            System.out.println("Saved with ID: " + savedDoctor.getMedicalStaffID());
//
//            return "redirect:/doctors";
//        } catch (Exception e) {
//            System.out.println(" ERROR SAVING DOCTOR: " + e.getMessage());
//            e.printStackTrace();
//            model.addAttribute("error", "Could not save doctor: " + e.getMessage());
//            model.addAttribute("doctor", doctor);
//            return "doctors/form";
//        }
//    }
//
//    // UPDATE ENDPOINT
//    @PostMapping("/update/{id}")
//    public String updateDoctor(@PathVariable String id, @ModelAttribute Doctor doctor, Model model) {
//        try {
//            doctorService.updateDoctor(id, doctor);
//            return "redirect:/doctors";
//        } catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("error", "Could not update doctor: " + e.getMessage());
//            model.addAttribute("doctor", doctor);
//            return "doctors/form";
//        }
//    }
//
//    @PostMapping("/{id}/delete")
//    public String deleteDoctor(@PathVariable String id) {
//        doctorService.deleteById(id);
//        return "redirect:/doctors";
//    }
//
//}
