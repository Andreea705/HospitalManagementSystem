package com.example.hospital.service;

import com.example.hospital.model.Appointments;
import com.example.hospital.repository.AppointmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentsService {

    private final AppointmentsRepository appointmentsRepo;

    @Autowired
    public AppointmentsService(AppointmentsRepository appointmentsRepo) {
        this.appointmentsRepo = appointmentsRepo;
    }

    public Appointments save(Appointments appointment) {
        return appointmentsRepo.save(appointment);
    }

    public List<Appointments> findAll() {
        return appointmentsRepo.findAll();
    }

    public Optional<Appointments> findById(String id) {
        return appointmentsRepo.findById(id);
    }

    public void deleteById(String id) {
        appointmentsRepo.deleteById(id);
    }

    public boolean existsById(String id) {
        return appointmentsRepo.existsById(id);
    }

    public List<Appointments> findByDepartmentId(String departmentId) {
        return appointmentsRepo.findAll().stream()
                .filter(appointment -> departmentId.equals(appointment.getDepartmentId()))
                .toList();
    }
}
