package com.example.hospital.service;

import com.example.hospital.model.Nurse;
import com.example.hospital.repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NurseService {

    private final NurseRepository nurseRepo;

    @Autowired
    public NurseService(NurseRepository nurseRepo) {
        this.nurseRepo = nurseRepo;
    }

    public Nurse save(Nurse nurse) {
        return nurseRepo.save(nurse);
    }

    public List<Nurse> findAll() {
        return nurseRepo.findAll();
    }

    public Optional<Nurse> findById(String id) {
        return nurseRepo.findById(id);
    }

    public void deleteById(String id) {
        nurseRepo.deleteById(id);
    }

    public boolean existsById(String id) {
        return nurseRepo.existsById(id);
    }

}
