package com.example.hospital.controller;

import com.example.hospital.model.Room;
import com.example.hospital.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "rooms/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("room", new Room());
        return "rooms/form";
    }

    @PostMapping
    public String save(@ModelAttribute Room room) {
        roomService.createRoom(room);
        return "redirect:/rooms";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Room room = roomService.getRoomById(id);
        model.addAttribute("room", room);
        return "rooms/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable String id, @ModelAttribute Room room) {
        roomService.updateRoom(id, room);
        return "redirect:/rooms";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        roomService.deleteRoom(id);
        return "redirect:/rooms";
    }
}
