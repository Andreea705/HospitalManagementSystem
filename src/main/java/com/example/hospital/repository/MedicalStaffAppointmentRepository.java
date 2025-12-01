//package com.example.hospital.repository;
//
//import com.example.hospital.model.MedicalStaffAppointment;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class MedicalStaffAppointmentRepository extends InFileRepository<MedicalStaffAppointment, String> {
//
//    public MedicalStaffAppointmentRepository() {
//        super("medical_staff_appointments.json", MedicalStaffAppointment.class);
//    }
//
//    @Override
//    protected String getEntityId(MedicalStaffAppointment entity) {
//        if (entity.getMedicalStaffAppointmentId() == null || entity.getMedicalStaffAppointmentId().isEmpty()) {
//            String newId = "MSA_" + System.currentTimeMillis();
//            setEntityId(entity, newId);
//            return newId;
//        }
//        return entity.getMedicalStaffAppointmentId();
//    }
//
//    @Override
//    protected void setEntityId(MedicalStaffAppointment entity, String id) {
//        entity.setMedicalStaffAppointmentId(id);
//    }
//
//    @Override
//    protected String parseId(String id) {
//        return id;
//    }
//
//    public List<MedicalStaffAppointment> findByMedicalStaffId(String medicalStaffId) {
//        return findAll().stream()
//                .filter(m -> medicalStaffId.equals(m.getMedicalStaffId()))
//                .toList();
//    }
//
//    public List<MedicalStaffAppointment> findByAppointmentID(String appointmentID) {
//        return findAll().stream()
//                .filter(m -> appointmentID.equals(m.getAppointmentID()))
//                .toList();
//    }
//}