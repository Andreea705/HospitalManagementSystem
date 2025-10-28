package com.example.hospital.service;

import com.example.hospital.model.Room;
import com.example.hospital.repository.RepoRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRoom {
    private final RepoRoom repoRoom;

    @Autowired
    public ServiceRoom(RepoRoom repoRoom) {
        this.repoRoom = repoRoom;
    }

    public Room createRoom(Room room) {
        validateRoom(room);
        return repoRoom.save(room);
    }

    public List<Room> getAllRooms() {
        return repoRoom.findAll();
    }

    public Room getRoomById(String id) {
        Room room = repoRoom.findById(id);
        if (room == null) {
            throw new RuntimeException("Room not found with id: " + id);
        }
        return room;
    }

    public Room updateRoom(String id, Room updatedRoom) {
        Room existingRoom = getRoomById(id);

        existingRoom.setHospitalId(updatedRoom.getHospitalId());
        existingRoom.setCapacity(updatedRoom.getCapacity());
        existingRoom.setStatus(updatedRoom.getStatus());
        existingRoom.setStatus(updatedRoom.getStatus());
        existingRoom.setAppointments(updatedRoom.getAppointments());

        validateRoom(existingRoom);
        return repoRoom.save(existingRoom);
    }

    public boolean deleteRoom(String id) {
        if (repoRoom.findById(id) != null) {
            return repoRoom.deleteById(id);
        }
        return false;
    }

    private void validateRoom(Room room) {
        if (room.getId() == null || room.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be empty");
        }
        if (room.getHospitalId() == null || room.getHospitalId().trim().isEmpty()) {
            throw new IllegalArgumentException("Hospital ID cannot be empty");
        }
        if (room.getCapacity() <= 0) {
            throw new IllegalArgumentException("Room capacity must be positive");
        }
        if (room.getStatus() == null || (!room.getStatus().equals("Available") && !room.getStatus().equals("Occupied"))) {
            throw new IllegalArgumentException("Room status must be 'Available' or 'Occupied'");
        }
    }

    public List<Room> getAvailableRooms() {
        return getAllRooms().stream()
                .filter(room -> "Available".equals(room.getStatus()))
                .toList();
    }

    public List<Room> getOccupiedRooms() {
        return getAllRooms().stream()
                .filter(room -> "Occupied".equals(room.getStatus()))
                .toList();
    }

    public List<Room> getRoomsByHospital(String hospitalId) {
        return getAllRooms().stream()
                .filter(room -> hospitalId.equals(room.getHospitalId()))
                .toList();
    }

    public List<Room> getRoomsByType(String type) {
        return getAllRooms().stream()
                .filter(room -> type.equalsIgnoreCase(room.getStatus()))
                .toList();
    }

    public List<Room> getRoomsWithMinCapacity(double minCapacity) {
        return getAllRooms().stream()
                .filter(room -> room.getCapacity() >= minCapacity)
                .toList();
    }

    public boolean isRoomAvailable(String roomId) {
        Room room = getRoomById(roomId);
        return "Available".equals(room.getStatus());
    }

    public void markRoomAsOccupied(String roomId) {
        Room room = getRoomById(roomId);
        room.setStatus("Occupied");
        repoRoom.save(room);
    }

    public void markRoomAsAvailable(String roomId) {
        Room room = getRoomById(roomId);
        room.setStatus("Available");
        repoRoom.save(room);
    }

    public Room findSuitableRoom(double requiredCapacity, String preferredType) {
        return getAvailableRooms().stream()
                .filter(room -> room.getCapacity() >= requiredCapacity)
                .filter(room -> preferredType == null || preferredType.equals(room.getStatus()))
                .findFirst()
                .orElse(null);
    }

    public long getAvailableRoomCount() {
        return getAvailableRooms().size();
    }
}