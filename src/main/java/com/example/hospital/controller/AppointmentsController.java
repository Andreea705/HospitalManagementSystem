//package com.example.hospital.controller;
//
//import com.example.hospital.model.Appointments;
//import com.example.hospital.model.AppointmentStatus;
//import com.example.hospital.service.AppointmentsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//public class AppointmentsController {
//
//    private final AppointmentsService appointmentsService;
//
//    @Autowired
//    public AppointmentsController(AppointmentsService appointmentsService) {
//        this.appointmentsService = appointmentsService;
//    }
//
//    //lista appointments
//    @GetMapping("/appointments")
//    public String getAllAppointments(Model model) {
//        System.out.println("Appointments list accessed");
//        try {
//            model.addAttribute("appointments", appointmentsService.findAll());
//            System.out.println("Appointments found: " + appointmentsService.findAll().size());
//        } catch (Exception e) {
//            System.out.println("Error getting appointments: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return "appointments/index";
//    }
//
//    //Formular creare
//    @GetMapping("/appointments/new")
//    public String showCreateForm(Model model) {
//        System.out.println("Appointment form accessed");
//        try {
//            model.addAttribute("appointment",
//                    new Appointments("", "", "", "", AppointmentStatus.ACTIVE.name()));
//            model.addAttribute("statuses", AppointmentStatus.values());
//            System.out.println("New appointment object created");
//        } catch (Exception e) {
//            System.out.println("Error creating appointment form: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return "appointments/form";
//    }
//
//    @GetMapping("/appointments/edit/{id}")
//    public String showEditForm(@PathVariable String id, Model model) {
//        System.out.println("Edit appointment form accessed for ID: " + id);
//        try {
//            Appointments appointment = appointmentsService.findById(id)
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID: " + id));
//            model.addAttribute("appointment", appointment);
//            model.addAttribute("statuses", AppointmentStatus.values());
//            System.out.println("Appointment found for editing: " + appointment.getAppointmentId());
//        } catch (Exception e) {
//            System.out.println("Error loading appointment for edit: " + e.getMessage());
//            e.printStackTrace();
//            return "redirect:/appointments";
//        }
//        return "appointments/form";
//    }
//
//
//    //Salvare
//    @PostMapping("/appointments/update/{id}")
//    public String updateAppointment(@PathVariable String id, @ModelAttribute Appointments appointment, Model model) {
//        System.out.println("=== ATTEMPTING TO UPDATE APPOINTMENT ===");
//        System.out.println("Appointment ID: " + id);
//        System.out.println("Patient ID: " + appointment.getPatientId());
//        System.out.println("Department ID: " + appointment.getDepartmentId());
//        System.out.println("Admission Date: " + appointment.getAdmissionDate());
//        System.out.println("Status: " + appointment.getStatus());
//
//        try {
//            appointment.setAppointmentId(id);
//            Appointments updatedAppointment = appointmentsService.save(appointment);
//            System.out.println(" APPOINTMENT UPDATED SUCCESSFULLY!");
//            System.out.println("Updated appointment ID: " + updatedAppointment.getAppointmentId());
//
//            return "redirect:/appointments";
//        } catch (Exception e) {
//            System.out.println(" ERROR UPDATING APPOINTMENT: " + e.getMessage());
//            e.printStackTrace();
//            model.addAttribute("error", "Could not update appointment: " + e.getMessage());
//            model.addAttribute("appointment", appointment);
//            model.addAttribute("statuses", AppointmentStatus.values());
//            return "appointments/form";
//        }
//    }
//
//    //Ștergere
//    @PostMapping("/appointments/{id}/delete")
//    public String deleteAppointment(@PathVariable String id) {
//        System.out.println("Deleting appointment with ID: " + id);
//        try {
//            appointmentsService.deleteById(id);
//            System.out.println("Appointment deleted successfully");
//        } catch (Exception e) {
//            System.out.println("Error deleting appointment: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return "redirect:/appointments";
//    }
//
//    @GetMapping("/appointments/{id}")
//    public String findById(@PathVariable String id, Model model) {
//        var appointment = appointmentsService.findById(id).orElse(null);
//        model.addAttribute("appointment", appointment);
//        return "appointments/details";
//    }
//
//    @GetMapping("/appointments/{id}/exists")
//    public String existsById(@PathVariable String id) {
//        boolean exists = appointmentsService.existsById(id);
//        return exists ? "Appointment exists" : "Appointment not found";
//    }
//
//    @GetMapping("/appointments/department/{departmentId}")
//    public String findByDepartmentId(@PathVariable String departmentId, Model model) {
//        model.addAttribute("appointments", appointmentsService.findByDepartmentId(departmentId));
//        return "appointments/index";
//    }
//}

package com.example.hospital.controller;

import com.example.hospital.model.Appointments;
import com.example.hospital.model.AppointmentStatus;
import com.example.hospital.service.AppointmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appointments")
public class AppointmentsController {

    private final AppointmentsService appointmentsService;

    @Autowired
    public AppointmentsController(AppointmentsService appointmentsService) {
        this.appointmentsService = appointmentsService;
    }

    // Lista appointments
    @GetMapping
    public String getAllAppointments(Model model) {
        System.out.println("Appointments list accessed");
        try {
            model.addAttribute("appointments", appointmentsService.findAll());
            System.out.println("Appointments found: " + appointmentsService.findAll().size());
        } catch (Exception e) {
            System.out.println("Error getting appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return "appointments/index";
    }

    // Formular creare
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        System.out.println("Appointment form accessed");
        try {
            model.addAttribute("appointment", new Appointments());
            model.addAttribute("statuses", AppointmentStatus.values());
            System.out.println("New appointment object created");
        } catch (Exception e) {
            System.out.println("Error creating appointment form: " + e.getMessage());
            e.printStackTrace();
        }
        return "appointments/form";
    }

    // Formular editare
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        System.out.println("Edit appointment form accessed for ID: " + id);
        try {
            Appointments appointment = appointmentsService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID: " + id));
            model.addAttribute("appointment", appointment);
            model.addAttribute("statuses", AppointmentStatus.values());
            System.out.println("Appointment found for editing: " + appointment.getAppointmentId());
        } catch (Exception e) {
            System.out.println("Error loading appointment for edit: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/appointments";
        }
        return "appointments/form";
    }

    // Creare appointment nou
    @PostMapping
    public String createAppointment(@ModelAttribute Appointments appointment, Model model) {
        System.out.println("=== ATTEMPTING TO CREATE APPOINTMENT ===");
        System.out.println("Patient ID: " + appointment.getPatientId());
        System.out.println("Department ID: " + appointment.getDepartmentId());
        System.out.println("Admission Date: " + appointment.getAdmissionDate());
        System.out.println("Status: " + appointment.getStatus());

        try {
            Appointments savedAppointment = appointmentsService.save(appointment);
            System.out.println("APPOINTMENT CREATED SUCCESSFULLY!");
            System.out.println("Created appointment ID: " + savedAppointment.getAppointmentId());

            return "redirect:/appointments";
        } catch (Exception e) {
            System.out.println("ERROR CREATING APPOINTMENT: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Could not create appointment: " + e.getMessage());
            model.addAttribute("appointment", appointment);
            model.addAttribute("statuses", AppointmentStatus.values());
            return "appointments/form";
        }
    }

    // Update appointment
    @PostMapping("/update/{id}")
    public String updateAppointment(@PathVariable String id, @ModelAttribute Appointments appointment, Model model) {
        System.out.println("=== ATTEMPTING TO UPDATE APPOINTMENT ===");
        System.out.println("Appointment ID: " + id);
        System.out.println("Patient ID: " + appointment.getPatientId());
        System.out.println("Department ID: " + appointment.getDepartmentId());
        System.out.println("Admission Date: " + appointment.getAdmissionDate());
        System.out.println("Status: " + appointment.getStatus());

        try {
            appointment.setAppointmentId(id);
            Appointments updatedAppointment = appointmentsService.save(appointment);
            System.out.println("APPOINTMENT UPDATED SUCCESSFULLY!");
            System.out.println("Updated appointment ID: " + updatedAppointment.getAppointmentId());

            return "redirect:/appointments";
        } catch (Exception e) {
            System.out.println("ERROR UPDATING APPOINTMENT: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Could not update appointment: " + e.getMessage());
            model.addAttribute("appointment", appointment);
            model.addAttribute("statuses", AppointmentStatus.values());
            return "appointments/form";
        }
    }

    // Ștergere
    @PostMapping("/{id}/delete")
    public String deleteAppointment(@PathVariable String id) {
        System.out.println("Deleting appointment with ID: " + id);
        try {
            appointmentsService.deleteById(id);
            System.out.println("Appointment deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/appointments";
    }

    // Detalii
    @GetMapping("/{id}")
    public String findById(@PathVariable String id, Model model) {
        var appointment = appointmentsService.findById(id).orElse(null);
        model.addAttribute("appointment", appointment);
        return "appointments/details";
    }

    @GetMapping("/{id}/exists")
    @ResponseBody
    public String existsById(@PathVariable String id) {
        boolean exists = appointmentsService.existsById(id);
        return exists ? "Appointment exists" : "Appointment not found";
    }

    @GetMapping("/department/{departmentId}")
    public String findByDepartmentId(@PathVariable String departmentId, Model model) {
        model.addAttribute("appointments", appointmentsService.findByDepartmentId(departmentId));
        return "appointments/index";
    }
}



