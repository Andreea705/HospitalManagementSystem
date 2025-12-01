package com.example.hospital.controller;

import com.example.hospital.model.Room;
import com.example.hospital.service.HospitalService;
import com.example.hospital.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final HospitalService hospitalService;

    public RoomController(RoomService roomService, HospitalService hospitalService) {
        this.roomService = roomService;
        this.hospitalService = hospitalService;
    }

    @GetMapping
    public String getAllRooms(@RequestParam(required = false) Long hospitalId, Model model) {
        if (hospitalId != null) {
            model.addAttribute("rooms", roomService.getRoomsByHospitalId(hospitalId));
            model.addAttribute("hospital", hospitalService.getHospitalById(hospitalId));
        } else {
            model.addAttribute("rooms", roomService.getAllRooms());
        }
        model.addAttribute("hospitals", hospitalService.getAllHospitals());
        return "rooms/index";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long hospitalId, Model model) {
        Room room = new Room();
        model.addAttribute("room", room);
        model.addAttribute("hospitals", hospitalService.getAllHospitals());
        model.addAttribute("selectedHospitalId", hospitalId);
        return "rooms/form";
    }

    @GetMapping("/{id}")
    public String getRoomById(@PathVariable Long id, Model model) {
        Room room = roomService.getRoomById(id);
        model.addAttribute("room", room);
        return "rooms/details";
    }

    @PostMapping
    public String createRoom(@Valid @ModelAttribute Room room,
                             BindingResult bindingResult,
                             @RequestParam Long hospitalId,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "rooms/form";
        }

        roomService.createRoom(room, hospitalId);
        return "redirect:/rooms?hospitalId=" + hospitalId;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Room room = roomService.getRoomById(id);
        model.addAttribute("room", room);
        model.addAttribute("hospitals", hospitalService.getAllHospitals());
        return "rooms/form";
    }

    @PostMapping("/update/{id}")
    public String updateRoom(@PathVariable Long id,
                             @Valid @ModelAttribute Room room,
                             BindingResult bindingResult,
                             @RequestParam(required = false) Long hospitalId,
                             Model model) {

        // Basic validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "rooms/form";
        }

        // Business validation: Check room number uniqueness for update
        if (!roomService.isRoomNumberUniqueForUpdate(room.getRoomNumber(), id)) {
            bindingResult.rejectValue("roomNumber", "error.room",
                    "Room number '" + room.getRoomNumber() + "' already exists. Please choose a different number.");
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "rooms/form";
        }

        // Additional validation: Capacity must be positive
        if (room.getCapacity() <= 0) {
            bindingResult.rejectValue("capacity", "error.room",
                    "Capacity must be greater than 0");
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "rooms/form";
        }

        roomService.updateRoom(id, room, hospitalId);

        // Redirect with context
        if (hospitalId != null) {
            return "redirect:/rooms?hospitalId=" + hospitalId;
        }
        return "redirect:/rooms";
    }

    @PostMapping("/{id}/delete")
    public String deleteRoom(@PathVariable Long id,
                             @RequestParam(required = false) Long hospitalId) {
        roomService.deleteRoom(id);

        if (hospitalId != null) {
            return "redirect:/rooms?hospitalId=" + hospitalId;
        }
        return "redirect:/rooms";
    }

    @PostMapping("/{id}/occupy")
    public String occupyRoom(@PathVariable Long id,
                             @RequestParam(required = false) Long hospitalId) {
        roomService.occupyRoom(id);

        if (hospitalId != null) {
            return "redirect:/rooms?hospitalId=" + hospitalId;
        }
        return "redirect:/rooms";
    }

    @PostMapping("/{id}/vacate")
    public String vacateRoom(@PathVariable Long id,
                             @RequestParam(required = false) Long hospitalId) {
        roomService.vacateRoom(id);

        if (hospitalId != null) {
            return "redirect:/rooms?hospitalId=" + hospitalId;
        }
        return "redirect:/rooms";
    }

    @PostMapping("/{id}/toggle")
    public String toggleAvailability(@PathVariable Long id,
                                     @RequestParam(required = false) Long hospitalId) {
        roomService.toggleAvailability(id);

        if (hospitalId != null) {
            return "redirect:/rooms?hospitalId=" + hospitalId;
        }
        return "redirect:/rooms";
    }
}