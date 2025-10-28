package com.example.hospital.controller;

import com.example.hospital.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class Controller {

    private final ServiceAppointments serviceAppointments;
    private final ServiceRoom serviceRoom;
    private final ServicePatient servicePatient;
    private final ServiceDoctor serviceDoctor;
    private final ServiceDepartment serviceDepartment;
    private final ServiceMedicalStaff serviceMedicalStaff;
    private final ServiceMedicalStaffAppointment serviceMedicalStaffAppointment;

    @Autowired
    public Controller(ServiceAppointments serviceAppointments,
                      ServiceRoom serviceRoom,
                      ServicePatient servicePatient,
                      ServiceDoctor serviceDoctor,
                      ServiceDepartment serviceDepartment,
                      ServiceMedicalStaff serviceMedicalStaff,
                      ServiceMedicalStaffAppointment serviceMedicalStaffAppointment) {
        this.serviceAppointments = serviceAppointments;
        this.serviceRoom = serviceRoom;
        this.servicePatient = servicePatient;
        this.serviceDoctor = serviceDoctor;
        this.serviceDepartment = serviceDepartment;
        this.serviceMedicalStaff = serviceMedicalStaff;
        this.serviceMedicalStaffAppointment = serviceMedicalStaffAppointment;
    }

    @GetMapping("/summary")
    public String getSummary() {
        long totalAppointments = serviceAppointments.getTotalAppointmentCount();
        long totalAvailableRooms = serviceRoom.getAvailableRoomCount();
        long totalPatients = servicePatient.getAllPatients().size();
        long totalDoctors = serviceDoctor.getAllDoctors().size();
        long totalDepartments = serviceDepartment.getAllDepartments().size();
        long totalStaff = serviceMedicalStaff.getAllMedicalStaff().size();
        long totalStaffAppointments = serviceMedicalStaffAppointment.getAllMedicalStaffAppointments().size();

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
        return serviceRoom.getAvailableRoomCount();
    }


    @GetMapping("/appointments/scheduled-count")
    public long getScheduledAppointments() {
        return serviceAppointments.getAppointmentCountByStatus("Scheduled");
    }


    @GetMapping("/medical-staff/count")
    public long getMedicalStaffCount() {
        return serviceMedicalStaff.getAllMedicalStaff().size();
    }


    @GetMapping("/departments/count")
    public long getDepartmentCount() {
        return serviceDepartment.getAllDepartments().size();
    }


    @GetMapping("/staff-appointments/count")
    public long getStaffAppointmentsCount() {
        return serviceMedicalStaffAppointment.getAllMedicalStaffAppointments().size();
    }
}

