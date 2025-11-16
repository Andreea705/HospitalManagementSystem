package com.example.hospital.dataconfigurations;

import com.example.hospital.model.Department;
import com.example.hospital.repository.DepartmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DepartmentDataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepo;

    public DepartmentDataInitializer(DepartmentRepository departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (departmentRepo.findAll().isEmpty()) {
            initializeSampleData();
        }
    }



    private void initializeSampleData() {
        Department[] sampleDepartments = {
                new Department("DEPT_1", "Cardiology", "HOSP_1", 8, "Dr. Smith"),
                new Department("DEPT_2", "Neurology", "HOSP_1", 6, "Dr. Johnson"),
                new Department("DEPT_3", "Pediatrics", "HOSP_1", 10, "Dr. Brown"),
                new Department("DEPT_4", "Oncology", "HOSP_2", 7, "Dr. Davis"),
                new Department("DEPT_5", "Orthopedics", "HOSP_2", 9, "Dr. Wilson"),
                new Department("DEPT_6", "Emergency", "HOSP_3", 12, "Dr. Miller"),
                new Department("DEPT_7", "Surgery", "HOSP_3", 5, "Dr. Taylor"),
                new Department("DEPT_8", "Radiology", "HOSP_4", 4, "Dr. Anderson"),
                new Department("DEPT_9", "Psychiatry", "HOSP_4", 8, "Dr. Thomas"),
                new Department("DEPT_10", "Dermatology", "HOSP_5", 3, "Dr. Jackson")
        };

        for (Department dept : sampleDepartments) {
            departmentRepo.save(dept);
        }
    }
}