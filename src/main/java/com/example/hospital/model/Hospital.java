package com.example.hospital.model;

import java.util.ArrayList;
import java.util.List;

public class Hospital {
    private String ID;
    private String name;
    private String city;
    private List<Department> departments = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();

    public Hospital(String ID, String name, String city) {
        this.ID = ID;
        this.name = name;
        this.city = city;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }


    public void addDepartment(Department department) {
        this.departments.add(department);
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }
}
