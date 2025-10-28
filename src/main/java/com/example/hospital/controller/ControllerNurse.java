package com.example.hospital.controller;

import com.example.hospital.model.Nurse;
import com.example.hospital.service.ServiceNurse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nurses")
public class ControllerNurse {

    private final ServiceNurse service;

    @Autowired
    public ControllerNurse(ServiceNurse service) {
        this.service = service;
    }

    @PostMapping
    public Nurse create(@RequestBody Nurse nurse) {
        return service.createNurse(nurse);
    }

    @GetMapping
    public List<Nurse> getAll() {
        return service.getAllNurses();
    }

    @GetMapping("/{qualification}")
    public Nurse getByQualification(@PathVariable String qualification) {
        return service.getNurseByQualificationLevel(qualification);
    }

    @PutMapping("/{qualification}")
    public Nurse update(@PathVariable String qualification, @RequestBody Nurse nurse) {
        return service.updateNurse(qualification, nurse);
    }

    @DeleteMapping("/{qualification}")
    public void delete(@PathVariable String qualification) {
        service.deleteNurse(qualification);
    }
}
