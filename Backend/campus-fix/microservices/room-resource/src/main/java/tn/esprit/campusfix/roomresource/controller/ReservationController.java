package tn.esprit.campusfix.roomresource.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "2. Gestion des Reservations", description = "Endpoints pour reserver des ressources, eviter les conflits et suivre le cycle de validation")
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "Creer une reservation", description = "Cree une demande de reservation pour une ou plusieurs ressources. Statut initial : EN_ATTENTE. Verifie la disponibilite et refuse si un conflit CONFIRMEE existe.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reservation creee avec succes"),
            @ApiResponse(responseCode = "400", description = "Dates invalides ou champs obligatoires manquants"),
            @ApiResponse(responseCode = "404", description = "Une des ressources demandees n'existe pas"),
            @ApiResponse(responseCode = "409", description = "Conflit detecte : ressource non active ou creneau deja reserve")
    })
    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation(@Valid @RequestBody ReservationRequestDTO dto) {
        ReservationResponseDTO created = reservationService.createReservation(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Lister les reservations", description = "Consulte l'historique des reservations avec filtrage optionnel par utilisateur, evenement ou statut.")
    @ApiResponse(responseCode = "200", description = "Liste des reservations recuperee")
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

    @Operation(summary = "Consulter une reservation par identifiant", description = "Renvoie les details d'une reservation et la liste des ressources associees.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservation trouvee"),
            @ApiResponse(responseCode = "404", description = "Reservation introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @Operation(summary = "Consulter les reservations d'un utilisateur", description = "Permet a un etudiant ou enseignant de consulter son historique de reservations.")
    @ApiResponse(responseCode = "200", description = "Reservations de l'utilisateur recuperees")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
    }

    @Operation(summary = "Mettre a jour le statut d'une reservation", description = "Permet a un administrateur de CONFIRMER ou REFUSER une reservation. En cas de confirmation, un reverification des conflits temporels est effectuee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statut mis a jour avec succes"),
            @ApiResponse(responseCode = "404", description = "Reservation introuvable"),
            @ApiResponse(responseCode = "409", description = "Impossible de confirmer : conflit de creneau apparu entre-temps")
    })
    @PatchMapping("/{id}/statut")
    public ResponseEntity<ReservationResponseDTO> updateStatut(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusDTO dto) {
        return ResponseEntity.ok(reservationService.updateReservationStatus(id, dto.getStatut()));
    }

    @Operation(summary = "Annuler une reservation", description = "Passe le statut de la reservation a ANNULEE, liberant immediatement les ressources pour d'autres usagers.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reservation annulee avec succes"),
            @ApiResponse(responseCode = "404", description = "Reservation introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
    }

    @Operation(summary = "Verifier la disponibilite d'une ressource", description = "Endpoint optimise pour la communication synchrone avec Event Service. Renvoie true si la ressource est libre, false sinon.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disponibilite evaluee"),
            @ApiResponse(responseCode = "400", description = "Dates invalides"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    @GetMapping("/disponibilite")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long ressourceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        boolean available = reservationService.checkAvailability(ressourceId, debut, fin);
        return ResponseEntity.ok(available);
    }
}
