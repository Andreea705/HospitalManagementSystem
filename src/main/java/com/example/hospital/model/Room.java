package com.example.hospital.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Room {
    private String id;
    private String roomNumber;
    private String type;
    private int capacity;
    private boolean available;
    private String hospitalId;
    private String status;

    public Room() {
    }

    @JsonCreator
    public Room(
            @JsonProperty("id") String id,
            @JsonProperty("roomNumber") String roomNumber,
            @JsonProperty("type") String type,
            @JsonProperty("capacity") int capacity,
            @JsonProperty("available") boolean available,
            @JsonProperty("hospitalId") String hospitalId,
            @JsonProperty("status") String status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.capacity = capacity;
        this.available = available;
        this.hospitalId = hospitalId != null ? hospitalId : "";
        this.status = status != null ? status : "Available";
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                ", available=" + available +
                ", hospitalId='" + hospitalId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getHospitalId() { return hospitalId; }
    public void setHospitalId(String hospitalId) { this.hospitalId = hospitalId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}