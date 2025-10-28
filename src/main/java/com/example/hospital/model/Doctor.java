package com.example.hospital.model;

public class Doctor {
    private String licenseNumber;
    private String name;//adaugat
    private String departmentID; //adaugat

    public Doctor(String licenseNumber, String name, String departmentID) {
        this.licenseNumber = licenseNumber;
        this.name = name;
        this.departmentID = departmentID;
    }

    public String getLicenseNumber() {return this.licenseNumber;}
    public void setLicenseNumber(String licenseNumber) {this.licenseNumber = licenseNumber;}

    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public String getDepartmentID() {return this.departmentID;}
    public void setDepartmentID(String departmentID) {this.departmentID = departmentID;}



}
