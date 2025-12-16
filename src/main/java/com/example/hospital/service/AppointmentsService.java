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

    //filtrat si sortat dupa statusul appointment-ului
    public List<Appointments> getFilteredAndSorted(String name, AppointmentStatus status, Long deptId, String sortField, String sortDir) {
        // Erstellung des Sort-Objekts für die Datenbank
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        // Leere Strings zu null konvertieren für die Query
        String nameFilter = (name == null || name.trim().isEmpty()) ? null : name;

        return appointmentsRepository.findByFilters(nameFilter, status, deptId, sort);
    }

    // ============ HELPER METHODS ============

    private void validateAppointmentData(Appointments appointment) {
        if (appointment.getAppointmentDate() == null) {
            throw new RuntimeException("Appointment date is required");
        }
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Appointment date cannot be in the past");
        }
    }

    public Appointments saveAppointment(Appointments appointment) {
        // Validate
        if (appointment.getAppointmentDate() == null) {
            throw new RuntimeException("Appointment date is required");
        }

        //Validare - verifica daca pacientul este in baza de date
        if (appointment.getPatient() == null || appointment.getPatient().getId() == null) {
            throw new RuntimeException("Patient is required");
        }
        Optional<Patient> patientOpt = patientRepository.findById(appointment.getPatient().getId());
        if(patientOpt.isEmpty()) {
            throw new RuntimeException("Patient not found with id: " + appointment.getPatient().getId() + "does not exist in the database");
        }
        Patient patient = patientOpt.get();

        //verifica daca departamentul exista in baza de date
        if (appointment.getDepartment() == null || appointment.getDepartment().getId() == null) {
            throw new RuntimeException("Department is required");
        }
        Optional<Department> departmentOpt = departmentRepository.findById(appointment.getDepartment().getId());
        if(departmentOpt.isEmpty()) {
            throw new RuntimeException("Department not found with id: " + appointment.getDepartment().getId() + "does not exist in the database");
        }
        Department department = departmentOpt.get();

        //validare - verifica daca doctorul exista
        Doctor doctor = null;
        if (appointment.getDoctor() != null && appointment.getDoctor().getId() != null) {
            Optional<Doctor> doctorOpt = doctorRepository.findById(appointment.getDoctor().getId());
            if(doctorOpt.isEmpty()) {
                throw new RuntimeException("Doctor not found with id: " + appointment.getDoctor().getId() + "does not exist in the database");
            }
            doctor = doctorOpt.get();
        }

        //validare-verifica daca doctorul apartine departamentului selectat
        if(!doctor.getDepartment().getId().equals(department.getId())) {
            throw new RuntimeException("Doctor does not belong to the department");
        }

        //validare-verifica statuusul programarile trebuie sa fie in viitor
        if(appointment.getAppointmentDate().isAfter(LocalDateTime.now())) {
            if(appointment.getStatus() != AppointmentStatus.ACTIVE) {
                throw new RuntimeException("Future appointments must be Active");
            }
        }

        //se sinc cu pacientul din obiectul Patient
        appointment.setPatientName(patient.getName());
        appointment.setPatient(patient);
        appointment.setDepartment(department);
        if(doctor != null) {
            appointment.setDoctor(doctor);
        }

        // Set default values
        if (appointment.getStatus() == null) {
            appointment.setStatus(AppointmentStatus.ACTIVE);
        }

        if (appointment.getCreatedAt() == null) {
            appointment.setCreatedAt(LocalDateTime.now());
        }

        appointment.setUpdatedAt(LocalDateTime.now());

        if(appointment.getId() == null) {
            appointment.setUpdatedAt(LocalDateTime.now());
        }
        appointment.setUpdatedAt(LocalDateTime.now());

        return appointmentsRepository.save(appointment);
    }

}