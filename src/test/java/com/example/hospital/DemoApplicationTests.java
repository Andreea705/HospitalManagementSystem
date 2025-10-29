package com.example.hospital;

import com.example.hospital.model.Department;
import com.example.hospital.model.Hospital;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
        Hospital hospital = new Hospital("123", "Hospital1", "Cluj");
        System.out.println(hospital.getCity());
        System.out.println(hospital.getName());

        Department department = new Department("111", "Pediatrie", "123", 30, "John Doe");
        hospital.addDepartment(department);
        System.out.println(hospital.getDepartments());
	}

}
