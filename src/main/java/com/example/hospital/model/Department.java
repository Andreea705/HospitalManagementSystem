package com.example.hospital.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

//la urmatorul lab de luat in considerare toate exceptiile, mesajele
public class Department {
    private String id;
    private String name;
    private String hospitalId;
    private int roomNumbers;
    private String departmentHead;

    public Department() {
    }

    @JsonCreator
    public Department(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("hospitalId") String hospitalId,
            @JsonProperty("roomNumbers") int roomNumbers,
            @JsonProperty("departmentHead") String departmentHead) {
        this.id = id;
        this.name = name;
        this.hospitalId = hospitalId;
        this.roomNumbers = roomNumbers;
        this.departmentHead = departmentHead;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", hospitalId='" + hospitalId + '\'' +
                ", roomNumbers=" + roomNumbers +
                ", departmentHead='" + departmentHead + '\'' +
                '}';
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getRoomNumbers() {
        return roomNumbers;
    }

    public void setRoomNumbers(int roomNumbers) {
        this.roomNumbers = roomNumbers;
    }

    public String getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(String departmentHead) {
        this.departmentHead = departmentHead;
    }

    public boolean hasCapacity() {
        return this.roomNumbers < 10;
    }


}

