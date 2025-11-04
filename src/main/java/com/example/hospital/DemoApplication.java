package com.example.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("Hospital Management System läuft!");
        System.out.println("Testen Sie die Anwendung unter: http://localhost:8080");
    }
}

