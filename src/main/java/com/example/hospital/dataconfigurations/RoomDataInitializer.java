package com.example.hospital.dataconfigurations;

import com.example.hospital.model.Room;
import com.example.hospital.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoomDataInitializer implements CommandLineRunner {

    private final RoomRepository roomRepository;

    public RoomDataInitializer(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roomRepository.findAll().isEmpty()) {
            initializeSampleData();
        }
    }

    private void initializeSampleData() {
        roomRepository.save(new Room("ROOM_1", "101", "Patient Room", 2, true, "HOSP_1", "Available"));
        roomRepository.save(new Room("ROOM_2", "102", "Patient Room", 2, true, "HOSP_1", "Available"));
        roomRepository.save(new Room("ROOM_3", "201", "Operating Room", 1, true, "HOSP_1", "Available"));
        roomRepository.save(new Room("ROOM_4", "202", "ICU", 1, true, "HOSP_1", "Available"));
        roomRepository.save(new Room("ROOM_5", "301", "Patient Room", 4, true, "HOSP_2", "Available"));
        roomRepository.save(new Room("ROOM_6", "302", "Patient Room", 2, true, "HOSP_2", "Available"));
        roomRepository.save(new Room("ROOM_7", "401", "Emergency Room", 6, true, "HOSP_3", "Available"));
        roomRepository.save(new Room("ROOM_8", "402", "Consultation Room", 3, true, "HOSP_3", "Available"));
        roomRepository.save(new Room("ROOM_9", "501", "Maternity Room", 2, true, "HOSP_4", "Available"));
        roomRepository.save(new Room("ROOM_10", "502", "Pediatric Room", 3, true, "HOSP_5", "Available"));
    }
}