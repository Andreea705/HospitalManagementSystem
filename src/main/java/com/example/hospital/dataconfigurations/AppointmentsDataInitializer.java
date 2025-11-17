package com.example.hospital.dataconfigurations;

import com.example.hospital.model.Appointments;
import com.example.hospital.model.Department;
import com.example.hospital.repository.AppointmentsRepository;
import com.example.hospital.repository.DepartmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppointmentsDataInitializer implements CommandLineRunner {

    private final AppointmentsRepository appointmentsRepository;

    public AppointmentsDataInitializer(AppointmentsRepository appointmentsRepository) {
        this.appointmentsRepository = appointmentsRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(appointmentsRepository.findAll().isEmpty()) {
            initializeSampleData();
        }
    }

    private void initializeSampleData() {
        Appointments[] sampleAppointments = {
                new Appointments("APP_1716301234567", "PATIENT_1", "DEPT_1", "2024-01-15", "ACTIVE"),
                new Appointments("APP_1716301234568", "PATIENT_2", "DEPT_2", "2024-01-16", "COMPLETED"),
                new Appointments("APP_1716301234569", "PATIENT_3", "DEPT_3", "2024-01-17", "ACTIVE"),
                new Appointments("APP_1716301234570", "PATIENT_1", "DEPT_10", "2024-01-18", "COMPLETED"),
                new Appointments("APP_1716301234571", "PATIENT_4", "DEPT_1", "2024-01-19", "COMPLETED"),
        };
        for (Appointments appointments : sampleAppointments) {
            appointmentsRepository.save(appointments);
        }

    }

}
