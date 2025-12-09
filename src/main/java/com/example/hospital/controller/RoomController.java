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

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final HospitalService hospitalService;

    public RoomController(RoomService roomService, HospitalService hospitalService) {
        this.roomService = roomService;
        this.hospitalService = hospitalService;
    }

    // ============ READ METHODS (unverändert) ============

    @GetMapping
    public String getAllRooms(@RequestParam(required = false) Long hospitalId, Model model) {
        if (hospitalId != null) {
            try {
                model.addAttribute("rooms", roomService.getRoomsByHospitalId(hospitalId));
                model.addAttribute("hospital", hospitalService.getHospitalById(hospitalId));
            } catch (RuntimeException e) {
                // Falls HospitalId nicht existiert
                model.addAttribute("errorMessage", e.getMessage());
                model.addAttribute("rooms", roomService.getAllRooms());
            }
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
    public String getRoomById(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Room room = roomService.getRoomById(id);
            model.addAttribute("room", room);
            return "rooms/details";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/rooms";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Room room = roomService.getRoomById(id);
            model.addAttribute("room", room);
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "rooms/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/rooms";
        }
    }

    // ============ CREATE ROOM ============

    @PostMapping
    public String createRoom(@Valid @ModelAttribute Room room,
                             BindingResult bindingResult,
                             @RequestParam Long hospitalId,
                             Model model) {

        // 1. Format/Struktur Validierung (@NotBlank, etc.)
        if (bindingResult.hasErrors()) {
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "rooms/form";
        }

        try {
            // 2. Business Validierung und Speicherung (Uniqueness, Hospital Existenz, Capacity > 0)
            roomService.createRoom(room, hospitalId);
            return "redirect:/rooms?hospitalId=" + hospitalId;
        } catch (RuntimeException e) {
            // Fängt alle Business-Regel-Verstöße vom Service ab
            handleServiceError(e, bindingResult, model, hospitalId, room);
            return "rooms/form";
        }
    }

    // ============ UPDATE ROOM ============

    @PostMapping("/update/{id}")
    public String updateRoom(@PathVariable Long id,
                             @Valid @ModelAttribute Room room,
                             BindingResult bindingResult,
                             @RequestParam(required = false) Long hospitalId,
                             Model model) {

        room.setId(id); // Wichtig, um die ID für die Form beizubehalten

        // 1. Format/Struktur Validierung (@NotBlank, etc.)
        if (bindingResult.hasErrors()) {
            model.addAttribute("hospitals", hospitalService.getAllHospitals());
            return "rooms/form";
        }

        try {
            // 2. Business Validierung und Update
            roomService.updateRoom(id, room, hospitalId);

            // Redirect with context
            if (hospitalId != null) {
                return "redirect:/rooms?hospitalId=" + hospitalId;
            }
            return "redirect:/rooms";
        } catch (RuntimeException e) {
            // Fängt alle Business-Regel-Verstöße vom Service ab
            handleServiceError(e, bindingResult, model, hospitalId, room);
            return "rooms/form";
        }
    }

    // ============ DELETE ROOM ============

    @PostMapping("/{id}/delete")
    public String deleteRoom(@PathVariable Long id,
                             @RequestParam(required = false) Long hospitalId,
                             RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteRoom(id);
            redirectAttributes.addFlashAttribute("successMessage", "Room successfully deleted.");
        } catch (RuntimeException e) {
            // Fängt "Room not found" oder "Cannot delete because it is currently occupied." ab
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (hospitalId != null) {
            return "redirect:/rooms?hospitalId=" + hospitalId;
        }
        return "redirect:/rooms";
    }

    // ============ BUSINESS LOGIC (Zustandswechsel) ============

    @PostMapping("/{id}/occupy")
    public String occupyRoom(@PathVariable Long id,
                             @RequestParam(required = false) Long hospitalId,
                             RedirectAttributes redirectAttributes) {
        try {
            roomService.occupyRoom(id);
            redirectAttributes.addFlashAttribute("successMessage", "Room occupied successfully.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return getRedirectPath(hospitalId);
    }

    @PostMapping("/{id}/vacate")
    public String vacateRoom(@PathVariable Long id,
                             @RequestParam(required = false) Long hospitalId,
                             RedirectAttributes redirectAttributes) {
        try {
            roomService.vacateRoom(id);
            redirectAttributes.addFlashAttribute("successMessage", "Room vacated successfully.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return getRedirectPath(hospitalId);
    }

    @PostMapping("/{id}/toggle")
    public String toggleAvailability(@PathVariable Long id,
                                     @RequestParam(required = false) Long hospitalId,
                                     RedirectAttributes redirectAttributes) {
        try {
            roomService.toggleAvailability(id);
            redirectAttributes.addFlashAttribute("successMessage", "Room availability toggled.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return getRedirectPath(hospitalId);
    }

    // ============ HILFSMETHODEN ============

    private String getRedirectPath(Long hospitalId) {
        if (hospitalId != null) {
            return "redirect:/rooms?hospitalId=" + hospitalId;
        }
        return "redirect:/rooms";
    }

    /**
     * Behandelt RunTimeExceptions aus dem Service und weist Fehler dem BindingResult zu.
     */
    private void handleServiceError(RuntimeException e, BindingResult bindingResult, Model model, Long hospitalId, Room room) {
        String message = e.getMessage();

        // Spezifische Fehler den Feldern zuweisen (für Thymeleaf Anzeige neben dem Feld)
        if (message.contains("Room number")) {
            bindingResult.rejectValue("roomNumber", "error.room.uniqueness", message);
        } else if (message.contains("Capacity")) {
            bindingResult.rejectValue("capacity", "error.room.capacity", message);
        } else if (message.contains("Hospital not found") || message.contains("Hospital ID must be provided")) {
            // Wenn der Fehler das Hospital betrifft, verwenden wir das allgemeine Model-Attribut oder
            // den RedirectAttributes (bei Redirects). Hier im Formular als allgemeiner Fehler.
            model.addAttribute("generalError", message);
        } else {
            // Generische Fehler
            model.addAttribute("generalError", "Operation failed: " + message);
        }

        model.addAttribute("hospitals", hospitalService.getAllHospitals());
        model.addAttribute("selectedHospitalId", hospitalId);
    }
}