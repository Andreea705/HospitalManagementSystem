package com.example.hospital.model;

public class Doctor extends MedicalStaff {
    private String licenseNumber;
    private String specialization;


    public Doctor() {
        super();
    }

    public Doctor(String medicalStaffID, String medicalStaffName, String departmentID,
                  String licenseNumber, String specialization) {
        super(medicalStaffID, medicalStaffName, departmentID, "doctor");
        this.licenseNumber = licenseNumber;
        this.specialization = specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "ID='" + getMedicalStaffID() + '\'' +
                ", Name='" + getMedicalStaffName() + '\'' +
                ", Department='" + getDepartamentID() + '\'' +
                ", LicenseNumber='" + licenseNumber + '\'' +
                ", Specialization='" + specialization + '\'' +
                '}';
    }
}

