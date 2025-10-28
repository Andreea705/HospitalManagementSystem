package com.example.hospital.model;

public class Nurse {
    private String qualificationLevel;
    private String shift; //adaugat
    private boolean onDuty; //adaugat

    public Nurse(String qualificationLevel, String shift, boolean onDuty) {
        this.qualificationLevel = qualificationLevel;
        this.shift = shift;
        this.onDuty = onDuty;
    }

    public String getQualificationLevel() {return qualificationLevel;}

    public void setQualificationLevel(String qualificationLevel) {this.qualificationLevel = qualificationLevel;}

    public String getShift() {return shift;}

    public void setShift(String shift) {this.shift = shift;}

    public boolean isOnDuty() {return onDuty;}

    public void setOnDuty(boolean onDuty) {this.onDuty = onDuty;}

}
