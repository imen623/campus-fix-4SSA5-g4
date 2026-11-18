package tn.esprit.campusfix.roomresource.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.campusfix.roomresource.dto.RessourceRequestDTO;
import tn.esprit.campusfix.roomresource.dto.RessourceResponseDTO;
import tn.esprit.campusfix.roomresource.enums.RessourceStatus;
import tn.esprit.campusfix.roomresource.enums.RessourceType;
import tn.esprit.campusfix.roomresource.service.RessourceService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ressources")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RessourceController {

    private final RessourceService ressourceService;

    @PostMapping
    public ResponseEntity<RessourceResponseDTO> createRessource(@Valid @RequestBody RessourceRequestDTO dto) {
        RessourceResponseDTO created = ressourceService.createRessource(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<RessourceResponseDTO>> getAllRessources(
            @RequestParam(required = false) RessourceType type,
            @RequestParam(required = false) String categorie,
            @RequestParam(required = false) String localisation,
            @RequestParam(required = false) RessourceStatus statut) {

        if (type != null && categorie != null && localisation != null) {
            return ResponseEntity.ok(ressourceService.filterRessources(type, categorie, localisation));
        } else if (type != null) {
            return ResponseEntity.ok(ressourceService.getRessourcesByType(type));
        } else if (statut != null) {
            return ResponseEntity.ok(ressourceService.getRessourcesByStatut(statut));
        }

        return ResponseEntity.ok(ressourceService.getAllRessources());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RessourceResponseDTO> getRessourceById(@PathVariable Long id) {
        return ResponseEntity.ok(ressourceService.getRessourceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RessourceResponseDTO> updateRessource(
            @PathVariable Long id,
            @Valid @RequestBody RessourceRequestDTO dto) {
        return ResponseEntity.ok(ressourceService.updateRessource(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRessource(@PathVariable Long id) {
        ressourceService.deleteRessource(id);
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<RessourceResponseDTO> updateStatut(
            @PathVariable Long id,
            @RequestParam RessourceStatus statut) {
        return ResponseEntity.ok(ressourceService.updateStatut(id, statut));
    }

    @GetMapping("/disponibilite")
    public ResponseEntity<List<RessourceResponseDTO>> getAvailableRessources(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(ressourceService.getAvailableRessources(debut, fin));
    }
}
