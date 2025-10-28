package com.example.hospital.model;

import java.util.ArrayList;
import java.util.List;

public class Hospital {
    private String id;
    private String name;
    private String city;
    private List<Department> departments;
    private List<Room> rooms;

    public Hospital(String ID, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.departments = new ArrayList<>();
        this.rooms = new ArrayList<>();
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

    public boolean removeRoom(Room room) {
        return this.rooms.remove(room);
    }
}
