package com.example.hospital.repository;

import com.example.hospital.model.Room;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoomRepo {
    private final List<Room> rooms = new ArrayList<>();

    public Room save(Room room) {
        Room existingRoom = findById(room.getId());

        if (existingRoom != null) {

            existingRoom.setHospitalId(room.getHospitalId());
            existingRoom.setCapacity(room.getCapacity());
            existingRoom.setStatus(room.getStatus());
            existingRoom.setStatus(room.getStatus());
            existingRoom.setAppointments(room.getAppointments());
            return existingRoom;
        }
        else {
            rooms.add(room);
            return room;
        }
    }

    public List<Room> findAll() {
        return new ArrayList<>(rooms);
    }

    public Room findById(String id) {
        if (id == null) return null;

        for (Room room : rooms) {
            if (room != null && id.equals(room.getId())) {
                return room;
            }
        }
        return null;
    }

    public boolean deleteById(String id) {
        Room room = findById(id);
        if (room != null) {
            return rooms.remove(room);
        }
        return false;
    }

}
