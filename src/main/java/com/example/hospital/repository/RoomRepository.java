package com.example.hospital.repository;

import com.example.hospital.model.Room;
import org.springframework.stereotype.Repository;


@Repository
public class RoomRepository extends GenericRepository<Room, String> {

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected String getEntityId(Room room) {
        if (room.getId() == null || room.getId().isEmpty()) {
            return "ROOM_" + System.currentTimeMillis();
        }
        return room.getId();
    }

}
