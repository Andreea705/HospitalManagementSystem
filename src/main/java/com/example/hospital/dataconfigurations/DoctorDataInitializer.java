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
          new Doctor("DOC_3", "Dr. Andrei Andreea", "DEPT_6", "L12347", "cardiologie"),
          new Doctor("DOC_4", "Dr. Popescu Maria",  "DEPT_7", "L12348", "neurologie"),
          new Doctor("DOC_5", "Dr. Popescu Ana",  "DEPT_8", "L12349", "neurologie")  , new Doctor("DOC_6", "Dr. Antonescu Maria",  "DEPT_4", "L12350", "neurologie"), new Doctor("DOC_7", "Dr. Lidia Marin",  "DEPT_2", "L12351", "neurologie"),
                new Doctor("DOC_8", "Dr. Stefan Salvatire",  "DEPT_1", "L12352", "neurologie"),
                new Doctor("DOC_9", "Dr. Damon Salvatore",  "DEPT_3", "L12353", "neurologie"),
                new Doctor("DOC_10", "Dr. Elena Gilbert",  "DEPT_3", "L12354", "neurologie")
        };
        for (Doctor doctor : sampleDoctors) {
            doctorRepository.save(doctor);
        }
    }
}
