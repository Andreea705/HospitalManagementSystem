package com.example.hospital.service;

import com.example.hospital.model.Nurse;
import com.example.hospital.repository.NurseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NurseService {
    private final NurseRepo nurseRepo;

    @Autowired
    public NurseService(NurseRepo nurseRepo) {
        this.nurseRepo = nurseRepo;
    }

    public Nurse createNurse(Nurse nurse) {
        validateNurse(nurse);
        return nurseRepo.save(nurse);
    }

    public List<Nurse> getAllNurses() {
        return nurseRepo.findAllNurses();
    }

    public Nurse getNurseByQualificationLevel(String qualificationLevel) {
        Nurse nurse = nurseRepo.findByQualificationLevel(qualificationLevel);
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
        return nurseRepo.save(existingNurse);
    }

    public boolean deleteNurse(String qualificationLevel) {
        return nurseRepo.deleteByQualificationLevel(qualificationLevel);
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
        return nurseRepo.findByShift(shift);
    }

    public List<Nurse> getOnDutyNurses() {
        return nurseRepo.findByOnDuty(true);
    }

    public List<Nurse> getOffDutyNurses() {
        return nurseRepo.findByOnDuty(false);
    }

    public List<Nurse> findNursesByQualification(String qualification) {
        return getAllNurses().stream()
                .filter(nurse -> nurse.getQualificationLevel().equalsIgnoreCase(qualification))
                .toList();
    }

    public boolean nurseExists(String qualificationLevel) {
        return nurseRepo.findByQualificationLevel(qualificationLevel) != null;
    }

    public long getTotalNurseCount() {
        return getAllNurses().size();
    }


    public boolean changeShift(String qualificationLevel, String newShift) {
        Nurse nurse = getNurseByQualificationLevel(qualificationLevel);
        nurse.setShift(newShift);
        nurseRepo.save(nurse);
        return true;
    }

    public boolean setOnDutyStatus(String qualificationLevel, boolean onDuty) {
        Nurse nurse = getNurseByQualificationLevel(qualificationLevel);
        nurse.setOnDuty(onDuty);
        nurseRepo.save(nurse);
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
