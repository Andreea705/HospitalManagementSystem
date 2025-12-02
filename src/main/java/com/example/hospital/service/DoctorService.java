package com.example.hospital.service;

import com.example.hospital.model.AppointmentStatus;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.Department;
import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.repository.DepartmentRepository;
import com.example.hospital.repository.AppointmentsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
        // Setează rolul automat
        doctor.setRole("doctor");

        // Setează departamentul dacă este specificat
        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
            doctor.setDepartment(department);
        }

        // Validări de unicitate
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

    public List<Doctor> searchDoctors(String name, String specialization, String departmentIdStr) {
        Long departmentId = null;
        if (departmentIdStr != null && !departmentIdStr.isEmpty()) {
            try {
                departmentId = Long.parseLong(departmentIdStr);
            } catch (NumberFormatException e) {
            }
        }
        return doctorRepository.searchDoctors(name, specialization, departmentId);
    }

    public List<Doctor> getDoctorsWithoutDepartment() {
        return doctorRepository.findDoctorsWithoutDepartment();
    }

    // ============ UPDATE ============

    public Doctor updateDoctor(Long id, Doctor doctorDetails, Long departmentId) {
        Doctor doctor = getDoctorById(id);

        // Actualizează câmpurile de bază
        doctor.setMedicalStaffName(doctorDetails.getMedicalStaffName());
        doctor.setMedicalStaffId(doctorDetails.getMedicalStaffId());

        // Actualizează câmpurile specifice Doctor
        doctor.setSpecialization(doctorDetails.getSpecialization());
        doctor.setEmail(doctorDetails.getEmail());
        doctor.setPhone(doctorDetails.getPhone());
        doctor.setLicenseNumber(doctorDetails.getLicenseNumber());

        // Actualizează departamentul
        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
            doctor.setDepartment(department);
        } else {
            doctor.setDepartment(null);
        }

        // Validări de unicitate
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

        // Setează doctorul la null pentru toate programările sale
        appointmentRepository.findByDoctorId(id).forEach(appointment -> {
            appointment.setDoctor(null);
            appointmentRepository.save(appointment);
        });

        doctorRepository.deleteById(id);
    }

    // ============ VALIDARE ============

    private void validateDoctorUniqueness(Doctor doctor, Long excludeId) {
        // Verifică medicalStaffId unic
        if (doctor.getMedicalStaffId() != null && !doctor.getMedicalStaffId().isEmpty()) {
            Doctor existing = doctorRepository.findByMedicalStaffId(doctor.getMedicalStaffId()).stream()
                    .findFirst().orElse(null);
            if (existing != null && (excludeId == null || !existing.getId().equals(excludeId))) {
                throw new RuntimeException("Medical Staff ID '" + doctor.getMedicalStaffId() + "' already exists");
            }
        }

        // Verifică license number unic
        if (doctor.getLicenseNumber() != null && !doctor.getLicenseNumber().isEmpty()) {
            Doctor existing = doctorRepository.findByLicenseNumber(doctor.getLicenseNumber());
            if (existing != null && (excludeId == null || !existing.getId().equals(excludeId))) {
                throw new RuntimeException("License number '" + doctor.getLicenseNumber() + "' already exists");
            }
        }

        // Verifică email unic (opțional)
        if (doctor.getEmail() != null && !doctor.getEmail().isEmpty()) {
            Doctor existing = doctorRepository.findByEmail(doctor.getEmail());
            if (existing != null && (excludeId == null || !existing.getId().equals(excludeId))) {
                throw new RuntimeException("Email '" + doctor.getEmail() + "' already exists");
            }
        }
    }

    public boolean doctorExists(Long id) {
        return doctorRepository.existsById(id);
    }

    public boolean doctorExistsByMedicalStaffId(String medicalStaffId) {
        return doctorRepository.existsByMedicalStaffId(medicalStaffId);
    }

    public boolean licenseNumberExists(String licenseNumber) {
        return doctorRepository.existsByLicenseNumber(licenseNumber);
    }

    public long countDoctorsByDepartment(Long departmentId) {
        return doctorRepository.findByDepartmentId(departmentId).size();
    }

    public long countAllDoctors() {
        return doctorRepository.count();
    }

    // ============ BUSINESS LOGIC ============

    public List<Doctor> getAvailableDoctors(Long departmentId) {
        if (departmentId != null) {
            return doctorRepository.findByDepartmentId(departmentId);
        } else {
            return doctorRepository.findAll();
        }
    }

    public List<Doctor> getDoctorsWithSpecializationInDepartment(String specialization, Long departmentId) {
        return doctorRepository.findByDepartmentId(departmentId).stream()
                .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization))
                .toList();
    }
}