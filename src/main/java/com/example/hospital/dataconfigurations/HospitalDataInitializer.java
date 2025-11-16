package com.example.hospital.dataconfigurations;

import com.example.hospital.model.Hospital;
import com.example.hospital.repository.HospitalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class HospitalDataInitializer implements CommandLineRunner {

    private final HospitalRepository hospitalRepository;

    public HospitalDataInitializer(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if no data exists
        if (hospitalRepository.findAll().isEmpty()) {
            initializeSampleData();
        }
    }

    private void initializeSampleData() {
        List<Hospital> sampleHospitals = Arrays.asList(
                new Hospital("HOSP_1", "City General Hospital", "Berlin", null, null),
                new Hospital("HOSP_2", "University Medical Center", "Munich", null, null),
                new Hospital("HOSP_3", "Community Hospital", "Hamburg", null, null),
                new Hospital("HOSP_4", "St. Mary's Hospital", "Cologne", null, null),
                new Hospital("HOSP_5", "Children's Medical Center", "Frankfurt", null, null),
                new Hospital("HOSP_6", "Regional Hospital", "Stuttgart", null, null),
                new Hospital("HOSP_7", "Central Clinic", "Düsseldorf", null, null),
                new Hospital("HOSP_8", "North Medical Center", "Leipzig", null, null),
                new Hospital("HOSP_9", "South General Hospital", "Dortmund", null, null),
                new Hospital("HOSP_10", "Westside Medical", "Essen", null, null)
        );

        sampleHospitals.forEach(hospitalRepository::save);
    }
}