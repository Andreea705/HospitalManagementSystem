package com.example.hospital.controller;

import com.example.hospital.model.Room;
import com.example.hospital.service.HospitalService;
import com.example.hospital.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final HospitalService hospitalService;

    public RoomController(RoomService roomService, HospitalService hospitalService) {
        this.roomService = roomService;
        this.hospitalService = hospitalService;
    }

    // ============ 1. LISTE MIT SORTIERUNG & FILTERUNG [cite: 24, 106] ============
    @GetMapping
    public String findAll(
            @RequestParam(required = false) Long hospitalId,
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "roomNumber") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        List<Room> rooms = roomService.getFilteredAndSortedRooms(hospitalId, roomNumber, type, sortField, sortDir);
        model.addAttribute("rooms", rooms);

        model.addAttribute("hospitals", hospitalService.getAllHospitals(null, null, "name", "asc"));

        model.addAttribute("selectedHospitalId", hospitalId);
        model.addAttribute("roomNumber", roomNumber);
        model.addAttribute("type", type);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "rooms/index";
    }

    // ============ 2. DETAILS (Verhindert White Page) ============
    @GetMapping("/{id}")
    public String getRoomById(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Room room = roomService.getRoomById(id);
            model.addAttribute("room", room);
            return "rooms/details";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", "Raum nicht gefunden: " + e.getMessage());
            return "redirect:/rooms";
        }
    }

    // ============ 3. CREATE / ERSTELLEN ============
    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long hospitalId, Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("hospitals", hospitalService.getAllHospitals(null, null, "name", "asc"));
        model.addAttribute("selectedHospitalId", hospitalId);
        return "rooms/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Room room, BindingResult bindingResult,
                       @RequestParam Long hospitalId, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("hospitals", hospitalService.getAllHospitals(null, null, "name", "asc"));
            return "rooms/form";
        }
        try {
            roomService.createRoom(room, hospitalId);
            return "redirect:/rooms?hospitalId=" + hospitalId;
        } catch (RuntimeException e) {
            handleServiceError(e, bindingResult, model, hospitalId, room);
            return "rooms/form";
        }
    }

    // ============ 4. UPDATE / BEARBEITEN ============
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Room room = roomService.getRoomById(id);
            model.addAttribute("room", room);
            model.addAttribute("hospitals", hospitalService.getAllHospitals(null, null, "name", "asc"));
            return "rooms/form";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/rooms";
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Room room,
                         BindingResult result, @RequestParam(required = false) Long hospitalId, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("hospitals", hospitalService.getAllHospitals(null, null, "name", "asc"));
            return "rooms/form";
        }
        try {
            roomService.updateRoom(id, room, hospitalId);
            return "redirect:/rooms" + (hospitalId != null ? "?hospitalId=" + hospitalId : "");
        } catch (RuntimeException e) {
            handleServiceError(e, result, model, hospitalId, room);
            return "rooms/form";
        }
    }

    // ============ 5. DELETE / LÖSCHEN ============
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @RequestParam(required = false) Long hospitalId, RedirectAttributes ra) {
        try {
            roomService.deleteRoom(id);
            ra.addFlashAttribute("successMessage", "Raum erfolgreich gelöscht.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/rooms" + (hospitalId != null ? "?hospitalId=" + hospitalId : "");
    }

    // ============ 6. BUSINESS LOGIC (OCCUPY / VACATE) ============
    @PostMapping("/{id}/occupy")
    public String occupyRoom(@PathVariable Long id,
                             @RequestParam(required = false) Long hospitalId,
                             RedirectAttributes ra) {
        try {
            roomService.occupyRoom(id);
            ra.addFlashAttribute("successMessage", "Raum wurde belegt.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/rooms" + (hospitalId != null ? "?hospitalId=" + hospitalId : "");
    }

    @PostMapping("/{id}/vacate")
    public String vacateRoom(@PathVariable Long id,
                             @RequestParam(required = false) Long hospitalId,
                             RedirectAttributes ra) {
        try {
            roomService.vacateRoom(id);
            ra.addFlashAttribute("successMessage", "Raum ist nun wieder frei.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/rooms" + (hospitalId != null ? "?hospitalId=" + hospitalId : "");
    }

    // ============ HILFSMETHODEN ============
    private void handleServiceError(RuntimeException e, BindingResult result, Model model, Long hospitalId, Room room) {
        String msg = e.getMessage();
        if (msg.contains("Room number")) result.rejectValue("roomNumber", "error.room.uniqueness", msg);
        else if (msg.contains("Capacity")) result.rejectValue("capacity", "error.room.capacity", msg);
        else model.addAttribute("generalError", msg);

        model.addAttribute("hospitals", hospitalService.getAllHospitals(null, null, "name", "asc"));
        model.addAttribute("selectedHospitalId", hospitalId);
    }
}