package com.example.hospital.controller;

import com.example.hospital.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/dashboard")
    public class Controller {

        private final ServiceAppointments appointmentsService;
        private final ServicePatient patientService;
        private final ServiceDoctor doctorService;
        private final ServiceNurse nurseService;

        @Autowired
        public Controller(ServiceAppointments appointmentsService,
                          ServicePatient patientService,
                          ServiceDoctor doctorService,
                          ServiceNurse nurseService) {
            this.appointmentsService = appointmentsService;
            this.patientService = patientService;
            this.doctorService = doctorService;
            this.nurseService = nurseService;
        }

        @GetMapping("/summary")
        public String getSummary() {
            long totalAppointments = appointmentsService.getTotalAppointmentCount();
            long totalPatients = patientService.getTotalPatientCount();
            long totalDoctors = doctorService.getTotalDoctorCount();
            long totalNurses = nurseService.getTotalNurseCount();

            return "Summary: Appointments=" + totalAppointments; //+
            //        ", Patients=" + totalPatients +//
            //        ", Doctors=" +

        }
    }