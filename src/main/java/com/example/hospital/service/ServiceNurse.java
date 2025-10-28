package com.example.hospital.service;

import com.example.hospital.model.Nurse;
import com.example.hospital.repository.RepoNurse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceNurse {
    private final RepoNurse repoNurse;

    @Autowired
    public ServiceNurse(RepoNurse repoNurse) {
        this.repoNurse = repoNurse;
    }

    public Nurse createNurse(Nurse nurse) {
        validateNurse(nurse);
        return repoNurse.save(nurse);
    }

    public List<Nurse> getAllNurses() {
        return repoNurse.findAllNurses();
    }

    public Nurse getNurseByQualificationLevel(String qualificationLevel) {
        Nurse nurse = repoNurse.findByQualificationLevel(qualificationLevel);
        if (nurse == null) {
            throw new RuntimeException("Nurse not found with qualification level: " + qualificationLevel);
        }
        return nurse;
    }

    public Nurse updateNurse(String qualificationLevel, Nurse updatedNurse) {
        Nurse existingNurse = getNurseByQualificationLevel(qualificationLevel);

        existingNurse.setShift(updatedNurse.getShift());
        existingNurse.setOnDuty(updatedNurse.isOnDuty());

        validateNurse(existingNurse);
        return repoNurse.save(existingNurse);
    }

    public boolean deleteNurse(String qualificationLevel) {
        return repoNurse.deleteByQualificationLevel(qualificationLevel);
    }


    private void validateNurse(Nurse nurse) {
        if (nurse.getQualificationLevel() == null || nurse.getQualificationLevel().trim().isEmpty()) {
            throw new IllegalArgumentException("Qualification level cannot be empty");
        }
        if (nurse.getShift() == null || nurse.getShift().trim().isEmpty()) {
            throw new IllegalArgumentException("Shift cannot be empty");
        }

        List<String> validQualifications = List.of("Registered Nurse", "Medical Nurse", "Senior Nurse", "Head Nurse");
        if (!validQualifications.contains(nurse.getQualificationLevel())) {
            throw new IllegalArgumentException("Invalid qualification level: " + nurse.getQualificationLevel());
        }
    }

    public List<Nurse> getNursesByShift(String shift) {
        return repoNurse.findByShift(shift);
    }

    public List<Nurse> getOnDutyNurses() {
        return repoNurse.findByOnDuty(true);
    }

    public List<Nurse> getOffDutyNurses() {
        return repoNurse.findByOnDuty(false);
    }

    public List<Nurse> findNursesByQualification(String qualification) {
        return getAllNurses().stream()
                .filter(nurse -> nurse.getQualificationLevel().equalsIgnoreCase(qualification))
                .toList();
    }

    public boolean nurseExists(String qualificationLevel) {
        return repoNurse.findByQualificationLevel(qualificationLevel) != null;
    }

    public long getTotalNurseCount() {
        return getAllNurses().size();
    }


    public boolean changeShift(String qualificationLevel, String newShift) {
        Nurse nurse = getNurseByQualificationLevel(qualificationLevel);
        nurse.setShift(newShift);
        repoNurse.save(nurse);
        return true;
    }

    public boolean setOnDutyStatus(String qualificationLevel, boolean onDuty) {
        Nurse nurse = getNurseByQualificationLevel(qualificationLevel);
        nurse.setOnDuty(onDuty);
        repoNurse.save(nurse);
        return true;
    }

    public List<Nurse> findEmergencyResponseNurses() {
        return getOnDutyNurses().stream()
                .filter(nurse -> {
                    String qualification = nurse.getQualificationLevel();
                    return "Senior Nurse".equals(qualification) || "Head Nurse".equals(qualification);
                })
                .toList();
    }

}
