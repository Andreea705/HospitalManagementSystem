package com.example.hospital.service;

import com.example.hospital.model.Room;
import com.example.hospital.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepo;

    @Autowired
    public RoomService(RoomRepository roomRepo) {
        this.roomRepo = roomRepo;
    }

    public Room createRoom(Room room) {
        return roomRepo.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    public Room getRoomById(String id) {
        Optional<Room> room = roomRepo.findById(id);
        if (room.isEmpty()) {
            throw new RuntimeException("Room not found with id: " + id);
        }
        return room.get();
    }

    public Room updateRoom(String id, Room updatedRoom) {
        Room existingRoom = getRoomById(id);

        existingRoom.setCapacity(updatedRoom.getCapacity());
        existingRoom.setStatus(updatedRoom.getStatus());
        existingRoom.setHospitalId(updatedRoom.getHospitalId());

        return roomRepo.save(existingRoom);
    }

    public boolean deleteRoom(String id) {
        if (roomRepo.existsById(id)) {
            roomRepo.deleteById(id);
            return true;
        }
        return false;
    }

}