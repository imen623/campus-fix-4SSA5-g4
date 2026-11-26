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
@Tag(name = "1. Gestion des Ressources", description = "Endpoints pour administrer et consulter les salles et equipements universitaires")
public class RessourceController {

    private final RessourceService ressourceService;

    @Operation(summary = "Ajouter une nouvelle ressource", description = "Cree une salle ou un equipement dans l'inventaire.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ressource creee avec succes"),
            @ApiResponse(responseCode = "400", description = "Donnees invalides ou champs obligatoires manquants")
    })
    @PostMapping
    public ResponseEntity<RessourceResponseDTO> createRessource(@Valid @RequestBody RessourceRequestDTO dto) {
        RessourceResponseDTO created = ressourceService.createRessource(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Lister les ressources", description = "Consulte toutes les ressources avec filtrage optionnel par type, categorie, localisation ou statut.")
    @ApiResponse(responseCode = "200", description = "Liste des ressources recuperee")
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

    @Operation(summary = "Consulter une ressource par identifiant", description = "Renvoie les details d'une ressource a partir de son ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ressource trouvee"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RessourceResponseDTO> getRessourceById(@PathVariable Long id) {
        return ResponseEntity.ok(ressourceService.getRessourceById(id));
    }

    @Operation(summary = "Modifier une ressource existante", description = "Met a jour les informations d'une salle ou d'un equipement.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ressource mise a jour avec succes"),
            @ApiResponse(responseCode = "400", description = "Donnees invalides"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RessourceResponseDTO> updateRessource(
            @PathVariable Long id,
            @Valid @RequestBody RessourceRequestDTO dto) {
        return ResponseEntity.ok(ressourceService.updateRessource(id, dto));
    }

    @Operation(summary = "Supprimer une ressource", description = "Retire une ressource de la base de donnees.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ressource supprimee avec succes"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRessource(@PathVariable Long id) {
        ressourceService.deleteRessource(id);
    }

    @Operation(summary = "Modifier l'etat/statut d'une ressource", description = "Permet de passer une ressource a ACTIVE, EN_MAINTENANCE ou INACTIVE.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statut modifie avec succes"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    @PatchMapping("/{id}/statut")
    public ResponseEntity<RessourceResponseDTO> updateStatut(
            @PathVariable Long id,
            @RequestParam RessourceStatus statut) {
        return ResponseEntity.ok(ressourceService.updateStatut(id, statut));
    }

    @Operation(summary = "Consulter les ressources disponibles pour une periode", description = "Calcule et renvoie la liste des ressources actives qui n'ont aucun chevauchement avec une reservation confirmee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des ressources disponibles"),
            @ApiResponse(responseCode = "400", description = "Dates invalides (dateDebut >= dateFin)")
    })
    @GetMapping("/disponibilite")
    public ResponseEntity<List<RessourceResponseDTO>> getAvailableRessources(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(ressourceService.getAvailableRessources(debut, fin));
    }
}
