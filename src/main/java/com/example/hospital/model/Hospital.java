package com.example.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hospitals")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // WICHTIG: Ändere von String zu Long!

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "City is required")
    @Column(nullable = false)
    private String city;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Department> departments = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();

    // ============ Konstruktoren ============

    public Hospital() {

    }

    public Hospital(String name, String city, List<Department> departments, List<Room> rooms) {
        this.name = name;
        this.city = city;
        this.departments = departments != null ? departments : new ArrayList<>();
        this.rooms = rooms != null ? rooms : new ArrayList<>();
    }


    // ============ Getter und Setter ============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    // ============ Hilfsmethoden ============

    public void addDepartment(Department department) {
        departments.add(department);
        department.setHospital(this);
    }

    public void removeDepartment(Department department) {
        departments.remove(department);
        department.setHospital(null);
    }

    public void addRoom(Room room) {
        rooms.add(room);
        room.setHospital(this);
    }

    public boolean removeRoom(Room room) {
        return rooms.remove(room);
    }

    // ============ toString ============

    @Override
    public String toString() {
        return "Hospital{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", departmentsCount=" + departments.size() +
                ", roomsCount=" + rooms.size() +
                '}';
    }
}