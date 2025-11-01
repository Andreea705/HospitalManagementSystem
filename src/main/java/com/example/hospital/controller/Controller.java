package com.example.hospital.controller;

import com.example.hospital.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class Controller {

    private final AppointmentsService appointmentsService;
    private final RoomService roomService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final DepartmentService departmentService;
    private final MedicalStaffService medicalStaffService;
    private final MedicalStaffAppointmentService medicalStaffAppointmentService;

    @Autowired
    public Controller(AppointmentsService appointmentsService,
                      RoomService roomService,
                      PatientService patientService,
                      DoctorService doctorService,
                      DepartmentService departmentService,
                      MedicalStaffService medicalStaffService,
                      MedicalStaffAppointmentService medicalStaffAppointmentService) {
        this.appointmentsService = appointmentsService;
        this.roomService = roomService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.departmentService = departmentService;
        this.medicalStaffService = medicalStaffService;
        this.medicalStaffAppointmentService = medicalStaffAppointmentService;
    }

    @GetMapping("/summary")
    public String getSummary() {
        long totalAppointments = appointmentsService.getTotalAppointmentCount();
        long totalAvailableRooms = roomService.getAvailableRoomCount();
        long totalPatients = patientService.getAllPatients().size();
        long totalDoctors = doctorService.getAllDoctors().size();
        long totalDepartments = departmentService.getAllDepartments().size();
        long totalStaff = medicalStaffService.getAllMedicalStaff().size();
        long totalStaffAppointments = medicalStaffAppointmentService.getAllMedicalStaffAppointments().size();

        return """
                🏥 Hospital Dashboard Summary:
                -----------------------------------
                • Total Appointments: %d
                • Available Rooms: %d
                • Total Patients: %d
                • Total Doctors: %d
                • Total Departments: %d
                • Total Medical Staff: %d
                • Total Staff Appointments: %d
                """.formatted(
                totalAppointments,
                totalAvailableRooms,
                totalPatients,
                totalDoctors,
                totalDepartments,
                totalStaff,
                totalStaffAppointments
        );
    }

    @GetMapping("/rooms/available-count")
    public long getAvailableRoomCount() {
        return roomService.getAvailableRoomCount();
    }


    @GetMapping("/appointments/scheduled-count")
    public long getScheduledAppointments() {
        return appointmentsService.getAppointmentCountByStatus("Scheduled");
    }


    @GetMapping("/medical-staff/count")
    public long getMedicalStaffCount() {
        return medicalStaffService.getAllMedicalStaff().size();
    }


    @GetMapping("/departments/count")
    public long getDepartmentCount() {
        return departmentService.getAllDepartments().size();
    }


    @GetMapping("/staff-appointments/count")
    public long getStaffAppointmentsCount() {
        return medicalStaffAppointmentService.getAllMedicalStaffAppointments().size();
    }
}

