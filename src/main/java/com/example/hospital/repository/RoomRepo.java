package com.example.hospital.repository;

import com.example.hospital.model.Room;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class RoomRepo extends GenericRepo<Room, String> {

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
