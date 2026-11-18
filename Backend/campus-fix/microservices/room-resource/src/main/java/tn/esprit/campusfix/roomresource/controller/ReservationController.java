package tn.esprit.campusfix.roomresource.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.campusfix.roomresource.dto.ReservationRequestDTO;
import tn.esprit.campusfix.roomresource.dto.ReservationResponseDTO;
import tn.esprit.campusfix.roomresource.dto.UpdateStatusDTO;
import tn.esprit.campusfix.roomresource.enums.ReservationStatus;
import tn.esprit.campusfix.roomresource.service.ReservationService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation(@Valid @RequestBody ReservationRequestDTO dto) {
        ReservationResponseDTO created = reservationService.createReservation(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) ReservationStatus statut) {

        if (userId != null) {
            return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
        } else if (eventId != null) {
            return ResponseEntity.ok(reservationService.getReservationsByEventId(eventId));
        } else if (statut != null) {
            return ResponseEntity.ok(reservationService.getReservationsByStatut(statut));
        }

        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<ReservationResponseDTO> updateStatut(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusDTO dto) {
        return ResponseEntity.ok(reservationService.updateReservationStatus(id, dto.getStatut()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
    }

    @GetMapping("/disponibilite")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long ressourceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        boolean available = reservationService.checkAvailability(ressourceId, debut, fin);
        return ResponseEntity.ok(available);
    }
}
