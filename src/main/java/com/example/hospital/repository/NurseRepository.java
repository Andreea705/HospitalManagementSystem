//package com.example.hospital.repository;
//
//import com.example.hospital.model.Nurse;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class NurseRepository extends InFileRepository<Nurse, String> {
//
//    public NurseRepository() {
//        super("nurses.json", Nurse.class);
//    }
//
//    @Override
//    protected String getEntityId(Nurse nurse) {
//        if (nurse.getMedicalStaffID() == null || nurse.getMedicalStaffID().isEmpty()) {
//            String newId = "NURSE_" + System.currentTimeMillis();
//            setEntityId(nurse, newId);
//            return newId;
//        }
//        return nurse.getMedicalStaffID();
//    }
//
//    @Override
//    protected void setEntityId(Nurse nurse, String id) {
//        nurse.setMedicalStaffID(id);
//    }
//
//    @Override
//    protected String parseId(String id) {
//        return id;
//    }
//
//    public List<Nurse> findByShift(String shift) {
//        return findAll().stream()
//                .filter(n -> n.getShift().equalsIgnoreCase(shift))
//                .toList();
//    }
//
//    public List<Nurse> findByOnDuty(boolean onDuty) {
//        return findAll().stream()
//                .filter(n -> n.isOnDuty() == onDuty)
//                .toList();
//    }
//}