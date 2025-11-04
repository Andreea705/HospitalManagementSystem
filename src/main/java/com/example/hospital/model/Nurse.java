package com.example.hospital.model;

public class Nurse extends MedicalStaff {
    private QualificationLevel qualificationLevel;
    private String shift;
    private boolean onDuty;
    public Nurse() {
        super();
    }

    public Nurse(String medicalStaffID, String medicalStaffName, String departmentID,
                 QualificationLevel qualificationLevel, String shift, boolean onDuty) {
        super(medicalStaffID, medicalStaffName, departmentID, "nurse");
        this.qualificationLevel = qualificationLevel;
        this.shift = shift;
        this.onDuty = onDuty;
    }

    public QualificationLevel getQualificationLevel() { return qualificationLevel; }
    public void setQualificationLevel(QualificationLevel qualificationLevel) { this.qualificationLevel = qualificationLevel; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public boolean isOnDuty() { return onDuty; }
    public void setOnDuty(boolean onDuty) { this.onDuty = onDuty; }

    @Override
    public String toString() {
        return "Nurse{" +
                "ID='" + getMedicalStaffID() + '\'' +
                ", Name='" + getMedicalStaffName() + '\'' +
                ", Department='" + getDepartmentID() + '\'' +
                ", QualificationLevel=" + qualificationLevel +
                ", Shift='" + shift + '\'' +
                ", OnDuty=" + onDuty +
                '}';
    }
}


