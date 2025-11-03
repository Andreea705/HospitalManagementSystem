package com.example.hospital.repository;
import com.example.hospital.model.Nurse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class NurseRepo extends GenericRepo<Nurse, String> {

    @Override
    protected String getEntityId(Nurse nurse) {
        if(nurse.getMedicalStaffID() == null || nurse.getMedicalStaffID().isEmpty()){
            return "NURSE" + System.currentTimeMillis();
        }
        return nurse.getMedicalStaffID();
    }

    @Override
    protected String parseId(String id){return id;}

    public List<Nurse> findByShift(String shift) {
        return storage.values().stream()
                .filter(n -> n.getShift().equalsIgnoreCase(shift))
                .toList();
    }

    public List<Nurse> findByOnDuty(boolean onDuty) {
        return storage.values().stream()
                .filter(Nurse::isOnDuty)
                .toList();
    }

}

