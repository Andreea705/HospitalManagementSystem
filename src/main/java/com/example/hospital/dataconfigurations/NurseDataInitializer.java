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
                new Nurse("NURSE_3", "Emily Davis", "DEPT_6", QualificationLevel.REGISTERED_NURSE, "Day", false),
                new Nurse("NURSE_4", "Emily Brown", "DEPT_5", QualificationLevel.PRACTICAL_NURSE, "Day", false),
                new Nurse("NURSE_5", "Andreea Davis", "DEPT_9", QualificationLevel.REGISTERED_NURSE, "Night", false),
                new Nurse("NURSE_6", "Jon Doe", "DEPT_1", QualificationLevel.PRACTICAL_NURSE, "Day", false),
                new Nurse("NURSE_7", "Emily Fields", "DEPT_3", QualificationLevel.REGISTERED_NURSE, "Day", true),
                new Nurse("NURSE_8", "Hannah Marin", "DEPT_8", QualificationLevel.PRACTICAL_NURSE, "Night", true),
                new Nurse("NURSE_9", "Aria Backer", "DEPT_4", QualificationLevel.REGISTERED_NURSE, "Day", false),
                new Nurse("NURSE_10", "Spancer Hastings", "DEPT_10", QualificationLevel.REGISTERED_NURSE, "Day", true)
        };
        for(Nurse nurse : sampleNurse) {
            nurseRepository.save(nurse);
        }
    }

}
