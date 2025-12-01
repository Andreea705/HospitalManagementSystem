package com.example.hospital.repository;

import com.example.hospital.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // ============ KORRIGIERTE METHODEN ============

    // FALSCH: findByHospitalId
    // RICHTIG: findByHospital_Id
    List<Room> findByHospital_Id(Long hospitalId);

    // Alternative mit @Query:
    @Query("SELECT r FROM Room r WHERE r.hospital.id = :hospitalId")
    List<Room> findByHospitalId(@Param("hospitalId") Long hospitalId);

    List<Room> findByAvailableTrue();

    // FALSCH: findByHospitalIdAndAvailableTrue
    // RICHTIG:
    @Query("SELECT r FROM Room r WHERE r.hospital.id = :hospitalId AND r.available = true")
    List<Room> findByHospitalIdAndAvailableTrue(@Param("hospitalId") Long hospitalId);

    Optional<Room> findByRoomNumber(String roomNumber);

    // ============ WEITERE METHODEN ============

    List<Room> findByRoomNumberContaining(String roomNumber);

    List<Room> findByType(String type);

    @Query("SELECT r FROM Room r WHERE r.type = :type AND r.hospital.id = :hospitalId")
    List<Room> findByTypeAndHospitalId(@Param("type") String type,
                                       @Param("hospitalId") Long hospitalId);

    @Query("SELECT r FROM Room r WHERE r.capacity >= :minCapacity")
    List<Room> findRoomsWithMinimumCapacity(@Param("minCapacity") int minCapacity);

    @Query("SELECT r FROM Room r WHERE r.available = true AND r.capacity >= :minCapacity")
    List<Room> findAvailableRoomsWithCapacity(@Param("minCapacity") int minCapacity);

    @Query("SELECT COUNT(r) FROM Room r WHERE r.hospital.id = :hospitalId")
    Long countByHospitalId(@Param("hospitalId") Long hospitalId);

    boolean existsByRoomNumber(String roomNumber);

    @Query("SELECT COUNT(r) > 0 FROM Room r WHERE r.roomNumber = :roomNumber AND r.id != :id")
    boolean existsByRoomNumberAndIdNot(@Param("roomNumber") String roomNumber,
                                       @Param("id") Long id);
}