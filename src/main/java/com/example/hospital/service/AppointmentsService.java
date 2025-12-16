package com.example.hospital.service;

import com.example.hospital.model.Appointments;
import com.example.hospital.model.Department;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.Patient;
import com.example.hospital.model.AppointmentStatus;
import com.example.hospital.repository.AppointmentsRepository;
import com.example.hospital.repository.DepartmentRepository;
import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//appointment sortat dupa status activ sau nu, sau daca a fost  completed sau nu pt. stergere
@Service
@Transactional
public class AppointmentsService {

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    // ============ CRUD OPERATIONS ============

    public List<Appointments> getAllAppointments() {
        return appointmentsRepository.findAll();
    }

    public Appointments getAppointmentById(Long id) {
        return appointmentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    }

    public Appointments updateAppointment(Long id, Appointments appointmentDetails) {
        Appointments appointment = getAppointmentById(id);

        validateAppointmentData(appointmentDetails);

        appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
        appointment.setPatientName(appointmentDetails.getPatientName());
        appointment.setDescription(appointmentDetails.getDescription());
        appointment.setStatus(appointmentDetails.getStatus());
        appointment.setUpdatedAt(LocalDateTime.now());

        // Update patient if provided
        if (appointmentDetails.getPatient() != null) {
            appointment.setPatient(appointmentDetails.getPatient());
        }

        return appointmentsRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        Appointments appointment = getAppointmentById(id);
        appointmentsRepository.delete(appointment);
    }

    public List<Appointments> getAppointmentsByDoctor(Long doctorId) {
        return appointmentsRepository.findByDoctorId(doctorId);
    }

    //filtrat si sortat dupa statusul appointment-ului + nume patient + posibil departament
    public List<Appointments> getFilteredAndSorted(String name, AppointmentStatus status, Long deptId, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        String nameFilter = (name == null || name.trim().isEmpty()) ? null : name;

        return appointmentsRepository.findByFilters(nameFilter, status, deptId, sort);
    }

    // ============ HELPER METHODS ============

    private void validateAppointmentData(Appointments appointment) {
        if (appointment.getAppointmentDate() == null) {
            throw new RuntimeException("Appointment date is required");
        }

        if (appointment.getId() == null && appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("New appointments must be in the future.");
        }
    }

    public Appointments saveAppointment(Appointments appointment) {
        //validare data
        validateAppointmentData(appointment);

        //validare patient
        if (appointment.getPatient() == null || appointment.getPatient().getId() == null) {
            throw new RuntimeException("Patient is required");
        }
        Patient patient = patientRepository.findById(appointment.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

       //validare department
        if (appointment.getDepartment() == null || appointment.getDepartment().getId() == null) {
            throw new RuntimeException("Department is required");
        }
        Department department = departmentRepository.findById(appointment.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

      //validare doctor
        Doctor doctor = null;
        if (appointment.getDoctor() != null && appointment.getDoctor().getId() != null) {
            doctor = doctorRepository.findById(appointment.getDoctor().getId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));

            if (!doctor.getDepartment().getId().equals(department.getId())) {
                throw new RuntimeException("Doctor does not belong to the selected department");
            }
        }

        if (appointment.getAppointmentDate().isAfter(LocalDateTime.now())) {
            //daca e in viitor, nu e COMPLETED (nu, zau...)
            if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
                throw new RuntimeException("A future appointment cannot be marked as COMPLETED.");
            }
        } else {
            if (appointment.getStatus() == AppointmentStatus.ACTIVE) {
            }
        }

        appointment.setPatientName(patient.getName());
        appointment.setPatient(patient);
        appointment.setDepartment(department);
        appointment.setDoctor(doctor);

        if (appointment.getId() == null) {
            appointment.setCreatedAt(LocalDateTime.now());
            if (appointment.getStatus() == null) {
                appointment.setStatus(AppointmentStatus.ACTIVE);
            }
        }
        appointment.setUpdatedAt(LocalDateTime.now());

        return appointmentsRepository.save(appointment);
    }
}