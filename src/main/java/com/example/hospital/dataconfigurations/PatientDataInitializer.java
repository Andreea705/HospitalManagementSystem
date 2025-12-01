//package com.example.hospital.dataconfigurations;
//
//import com.example.hospital.model.Patient;
//import com.example.hospital.repository.PatientRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//
//@Component
//public class PatientDataInitializer implements CommandLineRunner {
//
//    private final PatientRepository patientRepository;
//
//    public PatientDataInitializer(PatientRepository patientRepository) {
//        this.patientRepository = patientRepository;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        if (patientRepository.findAll().isEmpty()) {
//            initializeSampleData();
//        }
//    }
//
//    private void initializeSampleData() throws Exception {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//
//        patientRepository.save(new Patient(null, "John Smith", dateFormat.parse("15/05/1985"), "Male", "555-0101"));
//        patientRepository.save(new Patient(null, "Jane Doe", dateFormat.parse("22/11/1990"), "Female", "555-0102"));
//        patientRepository.save(new Patient(null, "Robert Johnson", dateFormat.parse("08/03/1978"), "Male", "555-0103"));
//        patientRepository.save(new Patient(null, "Maria Garcia", dateFormat.parse("30/07/1995"), "Female", "555-0104"));
//        patientRepository.save(new Patient(null, "David Brown", dateFormat.parse("12/12/1982"), "Male", "555-0105"));
//        patientRepository.save(new Patient(null, "Sarah Wilson", dateFormat.parse("25/09/1988"), "Female", "555-0106"));
//        patientRepository.save(new Patient(null, "Michael Chen", dateFormat.parse("03/01/1975"), "Male", "555-0107"));
//        patientRepository.save(new Patient(null, "Emily Davis", dateFormat.parse("18/06/1992"), "Female", "555-0108"));
//        patientRepository.save(new Patient(null, "James Miller", dateFormat.parse("14/04/1965"), "Male", "555-0109"));
//        patientRepository.save(new Patient(null, "Lisa Taylor", dateFormat.parse("29/10/1987"), "Female", "555-0110"));
//    }
//}