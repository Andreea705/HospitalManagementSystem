package com.example.hospital.service;

import com.example.hospital.model.Hospital;
import com.example.hospital.model.Room;
import com.example.hospital.repository.HospitalRepository;
import com.example.hospital.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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


        validateRoom(room, hospitalId, null);

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital not found with id: " + hospitalId));

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
        return roomRepository.findByHospital_Id(hospitalId);
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

    public List<Room> getAvailableRoomsWithCapacity(int minCapacity) {
        return roomRepository.findAvailableRoomsWithCapacity(minCapacity);
    }

    // ============ UPDATE ============

    public Room updateRoom(Long id, Room roomDetails, Long hospitalId) {
        Room room = getRoomById(id);

        validateRoom(roomDetails, hospitalId, id);

        // Update basic fields
        room.setRoomNumber(roomDetails.getRoomNumber());
        room.setType(roomDetails.getType());
        room.setCapacity(roomDetails.getCapacity());
        room.setAvailable(roomDetails.isAvailable());

        // Update hospital
        if (hospitalId != null) {
            Hospital hospital = hospitalRepository.findById(hospitalId)
                    .orElseThrow(() -> new RuntimeException("Hospital not found with id: " + hospitalId));
            room.setHospital(hospital);
        } else {
            room.setHospital(null);
        }

        // Update status if provided
        if (roomDetails.getStatus() != null) {
            room.setStatus(roomDetails.getStatus());
        }

        return roomRepository.save(room);
    }

    // ============ DELETE ============

    public void deleteRoom(Long id) {
        Room room = getRoomById(id);

        if (!room.isAvailable()) {
            throw new RuntimeException("Cannot delete room " + room.getRoomNumber() + " because it is currently occupied.");
        }

        roomRepository.delete(room);
    }

    // ============ BUSINESS LOGIC (Zustandswechsel) ============

    public Room occupyRoom(Long roomId) {
        Room room = getRoomById(roomId);

        // Business Rule: Raum muss verfügbar sein, um belegt zu werden
        if (!room.isAvailable()) {
            throw new RuntimeException("Room " + room.getRoomNumber() + " is already occupied.");
        }

        room.occupy();
        return roomRepository.save(room);
    }

    public Room vacateRoom(Long roomId) {
        Room room = getRoomById(roomId);

        if (room.isAvailable()) {
            throw new RuntimeException("Room " + room.getRoomNumber() + " is already vacant.");
        }

        room.vacate();
        return roomRepository.save(room);
    }

    public Room toggleAvailability(Long roomId) {
        Room room = getRoomById(roomId);

        if (!room.isAvailable()) {
            throw new RuntimeException("Cannot toggle availability: Room " + room.getRoomNumber() + " is currently occupied. Use 'vacateRoom' first.");
        }

        room.setAvailable(!room.isAvailable());
        return roomRepository.save(room);
    }

    // ============ ZENTRALE VALIDIERUNGSMETHODE ============

    private void validateRoom(Room room, Long hospitalId, Long excludeId) {

        if (hospitalId == null) {
            throw new RuntimeException("A Hospital ID must be provided.");
        }
        if (!hospitalRepository.existsById(hospitalId)) {
            throw new RuntimeException("Hospital not found with id: " + hospitalId);
        }

        if (room.getRoomNumber() != null && !room.getRoomNumber().trim().isEmpty()) {
            boolean exists;

            if (excludeId == null) {
                exists = roomRepository.existsByRoomNumber(room.getRoomNumber());
            } else {
                exists = roomRepository.existsByRoomNumberAndIdNot(room.getRoomNumber(), excludeId);
            }

            if (exists) {
                throw new RuntimeException("Room number '" + room.getRoomNumber() + "' already exists globally.");
            }
        }

        if (room.getCapacity() < 1) {
            throw new RuntimeException("Capacity must be greater than 0");
        }
    }
    public List<Room> getFilteredAndSortedRooms(Long hospitalId, String roomNumber, String type, String sortField, String sortDir) {
        // Sortierung vorbereiten
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        String filterNum = (roomNumber == null) ? "" : roomNumber;
        String filterType = (type == null) ? "" : type;

        if (hospitalId != null) {
            return roomRepository.findByHospital_IdAndRoomNumberContainingIgnoreCaseAndTypeContainingIgnoreCase(
                    hospitalId, filterNum, filterType, sort);
        }
        return roomRepository.findByRoomNumberContainingIgnoreCaseAndTypeContainingIgnoreCase(
                filterNum, filterType, sort);
    }
    // ============ COUNT/STATISTICS ============

    public long countRooms() {
        return roomRepository.count();
    }
}