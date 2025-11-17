package com.example.hospital.dataconfigurations;

import com.example.hospital.repository.MedicalStaffAppointmentRepository;
import com.example.hospital.model.MedicalStaffAppointment;

import org.springframework.boot.CommandLineRunner;

public class MedicalStaffAppointmentDataInitializer implements CommandLineRunner {
    private final MedicalStaffAppointmentRepository medicalStaffAppointmentRepository;

    public MedicalStaffAppointmentDataInitializer(MedicalStaffAppointmentRepository medicalStaffAppointmentRepository) {
        this.medicalStaffAppointmentRepository = medicalStaffAppointmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(medicalStaffAppointmentRepository.findAll().isEmpty()) {
            initializeSampleData();
        }
    }

    private void initializeSampleData() {
        MedicalStaffAppointment[] sampleMedicalStaffAppointments = {
                new MedicalStaffAppointment("MSA_6", "DOC_6","APP_1716301234571" ),
                new MedicalStaffAppointment("MSA_5", "DOC_5", "APP_1716301234570" ),
                new MedicalStaffAppointment("MSA_4", "DOC_4", "APP_1716301234569"),
                new MedicalStaffAppointment("MSA_9", "Nurse_1", "APP_1716301234569" ),
                new MedicalStaffAppointment("MSA_8", "Nurse_1", "APP_1716301234567"),
                new MedicalStaffAppointment("MSA_3", "DOC_3", "APP_1716301234568"),
        };
        for(MedicalStaffAppointment medicalStaffAppointment : sampleMedicalStaffAppointments) {
            medicalStaffAppointmentRepository.save(medicalStaffAppointment);
        }
    }
}
