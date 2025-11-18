package com.example.hospital.dataconfigurations;

import com.example.hospital.model.Nurse;
import com.example.hospital.model.QualificationLevel;
import com.example.hospital.repository.NurseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class NurseDataInitializer implements CommandLineRunner {
    private final NurseRepository nurseRepository;
    public NurseDataInitializer(NurseRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(nurseRepository.findAll().isEmpty()) {
            initializeSampleData();
        }
    }

    private void initializeSampleData() {
        Nurse[] sampleNurse = {
                new Nurse("NURSE_1", "Sarah Johnson", "DEPT_1", QualificationLevel.REGISTERED_NURSE, "Day", true),
                new Nurse("NURSE_2", "Michael Brown", "DEPT_3", QualificationLevel.PRACTICAL_NURSE, "Night", true),
                new Nurse("NURSE_3", "Emily Davis", "DEPT_6", QualificationLevel.REGISTERED_NURSE, "Day", false)
        };
        for(Nurse nurse : sampleNurse) {
            nurseRepository.save(nurse);
        }
    }

}
