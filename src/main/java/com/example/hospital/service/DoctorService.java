package com.example.hospital.service;

import com.example.hospital.model.AppointmentStatus;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.Department;
import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.repository.DepartmentRepository;
import com.example.hospital.repository.AppointmentsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final AppointmentsRepository appointmentRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository,
                         DepartmentRepository departmentRepository,
                         AppointmentsRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // ============ CREATE ============

    public Doctor createDoctor(Doctor doctor, Long departmentId) {
        // Seteaza rolul automat
        doctor.setRole("doctor");

        // Seteaza departamentul daca este specificat
        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
            doctor.setDepartment(department);
        }

        // Validari de unicitate
        validateDoctorUniqueness(doctor, null);

        return doctorRepository.save(doctor);
    }

    // ============ READ ============

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
    }

    public Doctor getDoctorByMedicalStaffId(String medicalStaffId) {
        List<Doctor> doctors = doctorRepository.findByMedicalStaffId(medicalStaffId);
        if (doctors.isEmpty()) {
            throw new RuntimeException("Doctor not found with Medical Staff ID: " + medicalStaffId);
        }
        return doctors.get(0);
    }

    public List<Doctor> getDoctorsByDepartment(Long departmentId) {
        return doctorRepository.findByDepartmentId(departmentId);
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationContainingIgnoreCase(specialization);
    }

    public List<Doctor> searchDoctors(
            String name,
            String specialization,
            Long departmentId,
            String sortBy,
            String sortDir) {

        Sort sort = Sort.by(
                sortDir.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                sortBy
        );

        return doctorRepository.searchDoctors(
                name,
                specialization,
                departmentId,
                sort
        );
    }

    public List<Doctor> getDoctorsWithoutDepartment() {
        return doctorRepository.findDoctorsWithoutDepartment();
    }

    // ============ UPDATE ============

    public Doctor updateDoctor(Long id, Doctor doctorDetails, Long departmentId) {
        Doctor doctor = getDoctorById(id);

        // Actualizeaza campurile de baza
        doctor.setMedicalStaffName(doctorDetails.getMedicalStaffName());
        doctor.setMedicalStaffId(doctorDetails.getMedicalStaffId());

        // Actualizeaza campurile specifice Doctor
        doctor.setSpecialization(doctorDetails.getSpecialization());
        doctor.setEmail(doctorDetails.getEmail());
        doctor.setPhone(doctorDetails.getPhone());
        doctor.setLicenseNumber(doctorDetails.getLicenseNumber());

        // Actualizeaza departamentul
        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
            doctor.setDepartment(department);
        } else {
            doctor.setDepartment(null);
        }

        // Validari de unicitate
        validateDoctorUniqueness(doctorDetails, id);

        return doctorRepository.save(doctor);
    }

    public Doctor assignToDepartment(Long doctorId, Long departmentId) {
        Doctor doctor = getDoctorById(doctorId);

        if (departmentId == null) {
            doctor.setDepartment(null);
        } else {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
            doctor.setDepartment(department);
        }

        return doctorRepository.save(doctor);
    }

    // ============ DELETE ============

    public void deleteDoctor(Long id) {
        Doctor doctor = getDoctorById(id);

        // Verifică dacă doctorul are programări viitoare ACTIVE
        long activeAppointments = appointmentRepository.findByDoctorId(id).stream()
                .filter(appointment -> appointment.getStatus() == AppointmentStatus.ACTIVE)
                .filter(appointment -> appointment.getAppointmentDate().isAfter(java.time.LocalDateTime.now()))
                .count();

        if (activeAppointments > 0) {
            throw new RuntimeException(
                    "Cannot delete doctor with " + activeAppointments + " active future appointments. " +
                            "Please reassign or cancel appointments first."
            );
        }

        // Setează doctorul la null pentru toate programarile sale
        appointmentRepository.findByDoctorId(id).forEach(appointment -> {
            appointment.setDoctor(null);
            appointmentRepository.save(appointment);
        });

        doctorRepository.deleteById(id);
    }

    // ============ VALIDARE ============

    private void validateDoctorUniqueness(Doctor doctor, Long excludeId) {
        // Verifica medicalStaffId unic
        if (doctor.getMedicalStaffId() != null && !doctor.getMedicalStaffId().isEmpty()) {
            Doctor existing = doctorRepository.findByMedicalStaffId(doctor.getMedicalStaffId()).stream()
                    .findFirst().orElse(null);
            if (existing != null && (excludeId == null || !existing.getId().equals(excludeId))) {
                throw new RuntimeException("Medical Staff ID '" + doctor.getMedicalStaffId() + "' already exists");
            }
        }

        // Verifica license number unic
        if (doctor.getLicenseNumber() != null && !doctor.getLicenseNumber().isEmpty()) {
            Doctor existing = doctorRepository.findByLicenseNumber(doctor.getLicenseNumber());
            if (existing != null && (excludeId == null || !existing.getId().equals(excludeId))) {
                throw new RuntimeException("License number '" + doctor.getLicenseNumber() + "' already exists");
            }
        }

        // Verifica email unic
        if (doctor.getEmail() != null && !doctor.getEmail().isEmpty()) {
            Doctor existing = doctorRepository.findByEmail(doctor.getEmail());
            if (existing != null && (excludeId == null || !existing.getId().equals(excludeId))) {
                throw new RuntimeException("Email '" + doctor.getEmail() + "' already exists");
            }
        }
    }

    //validarea departamentului existent
    private void validateDepartmentExists(Long departmentId) {
        if(departmentId != null){
            if(!departmentRepository.existsById(departmentId)){
                throw new RuntimeException("Department not found with id: " + departmentId);
            }
        }
    }

    public long countAllDoctors() {
        return doctorRepository.count();
    }
}