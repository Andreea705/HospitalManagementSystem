package com.example.hospital.service;

import com.example.hospital.model.Hospital;
import com.example.hospital.model.Room;
import com.example.hospital.repository.HospitalRepository;
import com.example.hospital.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final HospitalRepository hospitalRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, HospitalRepository hospitalRepository) {
        this.roomRepository = roomRepository;
        this.hospitalRepository = hospitalRepository;
    }

    // ============ CREATE ============

    public Room createRoom(Room room, Long hospitalId) {
        // Validate hospital exists
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital not found with id: " + hospitalId));

        // Validate room number is unique
        if (roomRepository.existsByRoomNumber(room.getRoomNumber())) {
            throw new RuntimeException("Room number already exists: " + room.getRoomNumber());
        }

        room.setHospital(hospital);
        return roomRepository.save(room);
    }

    // ============ READ ============

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }

    public Optional<Room> findRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public List<Room> getRoomsByHospitalId(Long hospitalId) {
        return roomRepository.findByHospitalId(hospitalId);
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findByAvailableTrue();
    }

    public List<Room> getAvailableRoomsByHospital(Long hospitalId) {
        return roomRepository.findByHospitalIdAndAvailableTrue(hospitalId);
    }

    public List<Room> getRoomsByType(String type) {
        return roomRepository.findByType(type);
    }

//    public List<Room> getRoomsByTypeAndHospital(String type, Long hospitalId) {
//        return roomRepository.findByTypeAndHospitalId(type, hospitalId);
//    }
//
//    public List<Room> searchRoomsByRoomNumber(String roomNumber) {
//        return roomRepository.findByRoomNumberContaining(roomNumber);
//    }
//
//    public Room getRoomByRoomNumber(String roomNumber) {
//        return roomRepository.findByRoomNumber(roomNumber)
//                .orElseThrow(() -> new RuntimeException("Room not found with number: " + roomNumber));
//    }
//
//    public List<Room> getRoomsWithMinimumCapacity(int minCapacity) {
//        return roomRepository.findRoomsWithMinimumCapacity(minCapacity);
//    }

    public List<Room> getAvailableRoomsWithCapacity(int minCapacity) {
        return roomRepository.findAvailableRoomsWithCapacity(minCapacity);
    }

    // ============ UPDATE ============

    public Room updateRoom(Long id, Room roomDetails, Long hospitalId) {
        Room room = getRoomById(id);

        // Validate room number is unique (excluding current room)
        if (!room.getRoomNumber().equals(roomDetails.getRoomNumber()) &&
                roomRepository.existsByRoomNumberAndIdNot(roomDetails.getRoomNumber(), id)) {
            throw new RuntimeException("Room number already exists: " + roomDetails.getRoomNumber());
        }

        // Update basic fields
        room.setRoomNumber(roomDetails.getRoomNumber());
        room.setType(roomDetails.getType());
        room.setCapacity(roomDetails.getCapacity());
        room.setAvailable(roomDetails.isAvailable());

        // Update hospital if provided
        if (hospitalId != null) {
            Hospital hospital = hospitalRepository.findById(hospitalId)
                    .orElseThrow(() -> new RuntimeException("Hospital not found with id: " + hospitalId));
            room.setHospital(hospital);
        }

        // Update status if provided
        if (roomDetails.getStatus() != null) {
            room.setStatus(roomDetails.getStatus());
        }

        return roomRepository.save(room);
    }

    // ============ DELETE ============

    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Room not found with id: " + id);
        }
        roomRepository.deleteById(id);
    }

    // ============ BUSINESS LOGIC ============

    public Room occupyRoom(Long roomId) {
        Room room = getRoomById(roomId);

        if (!room.isAvailable()) {
            throw new RuntimeException("Room " + room.getRoomNumber() + " is already occupied");
        }

        room.occupy();
        return roomRepository.save(room);
    }

    public Room vacateRoom(Long roomId) {
        Room room = getRoomById(roomId);
        room.vacate();
        return roomRepository.save(room);
    }

    public Room toggleAvailability(Long roomId) {
        Room room = getRoomById(roomId);
        room.setAvailable(!room.isAvailable());
        return roomRepository.save(room);
    }


    public boolean roomExists(Long id) {
        return roomRepository.existsById(id);
    }

    public boolean isRoomAvailable(Long roomId) {
        Room room = getRoomById(roomId);
        return room.isAvailable();
    }

    public boolean canRoomAccommodate(Long roomId, int numberOfPatients) {
        Room room = getRoomById(roomId);
        return room.canAccommodate(numberOfPatients);
    }

    // ============ COUNT/STATISTICS ============

    public long countRooms() {
        return roomRepository.count();
    }

    public boolean isRoomNumberUniqueForUpdate(String roomNumber, Long id) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            return true;
        }

        // If no exclude ID provided, check general uniqueness
        if (id == null) {
            return !roomRepository.existsByRoomNumber(roomNumber);
        }

        // Check if room number exists, excluding the current room
        return !roomRepository.existsByRoomNumberAndIdNot(roomNumber, id);


    }
}
