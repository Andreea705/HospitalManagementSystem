package com.example.hospital.controller;

import com.example.hospital.model.Room;
import com.example.hospital.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class ControllerRoom {

    private final RoomService roomService;

    @Autowired
    public ControllerRoom(RoomService roomService) {
        this.roomService = roomService;
    }


    @PostMapping
    public Room createRoom(@RequestBody Room room) {
        return roomService.createRoom(room);
    }


    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }


    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable String id) {
        return roomService.getRoomById(id);
    }


    @PutMapping("/{id}")
    public Room updateRoom(@PathVariable String id, @RequestBody Room updatedRoom) {
        return roomService.updateRoom(id, updatedRoom);
    }


    @DeleteMapping("/{id}")
    public boolean deleteRoom(@PathVariable String id) {
        return roomService.deleteRoom(id);
    }


    @GetMapping("/available")
    public List<Room> getAvailableRooms() {
        return roomService.getAvailableRooms();
    }


    @GetMapping("/occupied")
    public List<Room> getOccupiedRooms() {
        return roomService.getOccupiedRooms();
    }


    @GetMapping("/hospital/{hospitalId}")
    public List<Room> getRoomsByHospital(@PathVariable String hospitalId) {
        return roomService.getRoomsByHospital(hospitalId);
    }


    @GetMapping("/capacity/{min}")
    public List<Room> getRoomsByCapacity(@PathVariable double min) {
        return roomService.getRoomsWithMinCapacity(min);
    }


    @GetMapping("/{id}/available")
    public boolean isRoomAvailable(@PathVariable String id) {
        return roomService.isRoomAvailable(id);
    }


    @PutMapping("/{id}/occupy")
    public void markRoomAsOccupied(@PathVariable String id) {
        roomService.markRoomAsOccupied(id);
    }


    @PutMapping("/{id}/release")
    public void markRoomAsAvailable(@PathVariable String id) {
        roomService.markRoomAsAvailable(id);
    }


    @GetMapping("/count/available")
    public long getAvailableRoomCount() {
        return roomService.getAvailableRoomCount();
    }


    @GetMapping("/find")
    public Room findSuitableRoom(@RequestParam double capacity,
                                 @RequestParam(required = false) String preferredType) {
        return roomService.findSuitableRoom(capacity, preferredType);
    }
}
