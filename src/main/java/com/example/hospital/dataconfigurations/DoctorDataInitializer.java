package com.example.hospital.dataconfigurations;

import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.model.Doctor;
import org.springframework.boot.CommandLineRunner;

public class DoctorDataInitializer implements CommandLineRunner {

    private final DoctorRepository doctorRepository;

    public DoctorDataInitializer(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (doctorRepository.findAll().isEmpty()) {
            initializeSampleData();
        }
    }

    private void initializeSampleData() {
        Doctor[] sampleDoctors = {
          new Doctor("DOC_1", "Dr. Andrei Popescu", "DEPT_1", "L12345", "cardiologie"),
          new Doctor("DOC_2", "Dr. Adi Andrei", "DEPT_2", "L12346", "chirurgie"),
          new Doctor("DOC_3", "Dr. Andrei Andreea", "doctor", "L12347", "cardiologie"),
          new Doctor("DOC_4", "Dr. Popescu Maria",  "doctor", "L12348", "neurologie")
        };
        for (Doctor doctor : sampleDoctors) {
            doctorRepository.save(doctor);
        }
    }
}
