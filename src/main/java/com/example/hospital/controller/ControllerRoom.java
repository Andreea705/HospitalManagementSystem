package com.example.hospital.controller;

import com.example.hospital.model.Room;
import com.example.hospital.service.ServiceRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class ControllerRoom {

    private final ServiceRoom serviceRoom;

    @Autowired
    public ControllerRoom(ServiceRoom serviceRoom) {
        this.serviceRoom = serviceRoom;
    }


    @PostMapping
    public Room createRoom(@RequestBody Room room) {
        return serviceRoom.createRoom(room);
    }


    @GetMapping
    public List<Room> getAllRooms() {
        return serviceRoom.getAllRooms();
    }


    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable String id) {
        return serviceRoom.getRoomById(id);
    }


    @PutMapping("/{id}")
    public Room updateRoom(@PathVariable String id, @RequestBody Room updatedRoom) {
        return serviceRoom.updateRoom(id, updatedRoom);
    }


    @DeleteMapping("/{id}")
    public boolean deleteRoom(@PathVariable String id) {
        return serviceRoom.deleteRoom(id);
    }


    @GetMapping("/available")
    public List<Room> getAvailableRooms() {
        return serviceRoom.getAvailableRooms();
    }


    @GetMapping("/occupied")
    public List<Room> getOccupiedRooms() {
        return serviceRoom.getOccupiedRooms();
    }


    @GetMapping("/hospital/{hospitalId}")
    public List<Room> getRoomsByHospital(@PathVariable String hospitalId) {
        return serviceRoom.getRoomsByHospital(hospitalId);
    }


    @GetMapping("/capacity/{min}")
    public List<Room> getRoomsByCapacity(@PathVariable double min) {
        return serviceRoom.getRoomsWithMinCapacity(min);
    }


    @GetMapping("/{id}/available")
    public boolean isRoomAvailable(@PathVariable String id) {
        return serviceRoom.isRoomAvailable(id);
    }


    @PutMapping("/{id}/occupy")
    public void markRoomAsOccupied(@PathVariable String id) {
        serviceRoom.markRoomAsOccupied(id);
    }


    @PutMapping("/{id}/release")
    public void markRoomAsAvailable(@PathVariable String id) {
        serviceRoom.markRoomAsAvailable(id);
    }


    @GetMapping("/count/available")
    public long getAvailableRoomCount() {
        return serviceRoom.getAvailableRoomCount();
    }


    @GetMapping("/find")
    public Room findSuitableRoom(@RequestParam double capacity,
                                 @RequestParam(required = false) String preferredType) {
        return serviceRoom.findSuitableRoom(capacity, preferredType);
    }
}
