package com.example.hospital.repository;

import com.example.hospital.model.Room;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepository extends InFileRepository<Room, String> {

    public RoomRepository() {
        super("rooms.json", Room.class);
    }

    @Override
    protected String getEntityId(Room room) {
        return room.getId();
    }

    @Override
    protected void setEntityId(Room room, String id) {
        room.setId(id);
    }

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected String generateId() {

        return "ROOM_" + System.currentTimeMillis();
    }
}