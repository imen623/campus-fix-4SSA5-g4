package tn.esprit.campusfix.lostfound.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.campusfix.lostfound.dto.request.ReclamationRequestDTO;
import tn.esprit.campusfix.lostfound.dto.request.ReclamationStatutUpdateDTO;
import tn.esprit.campusfix.lostfound.dto.response.ReclamationResponseDTO;
import tn.esprit.campusfix.lostfound.service.ReclamationService;

import java.util.List;

@RestController
@Tag(name = "Réclamations", description = "Gestion des réclamations sur objets trouvés")
public class ReclamationController {

    private final ReclamationService reclamationService;

    public ReclamationController(ReclamationService reclamationService) {
        this.reclamationService = reclamationService;
    }

    @PostMapping("/api/reclamations")
    @Operation(summary = "Créer une réclamation sur un objet trouvé")
    public ResponseEntity<ReclamationResponseDTO> create(@Valid @RequestBody ReclamationRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reclamationService.createReclamation(request));
    }

    @GetMapping("/api/reclamations/{id}")
    @Operation(summary = "Obtenir une réclamation par ID")
    public ResponseEntity<ReclamationResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reclamationService.getReclamationById(id));
    }

    @PatchMapping("/api/reclamations/{id}/statut")
    @Operation(summary = "Mettre à jour le statut d'une réclamation (ACCEPTEE → déclaration RESOLUE)")
    public ResponseEntity<ReclamationResponseDTO> updateStatut(@PathVariable Long id,
                                                                @Valid @RequestBody ReclamationStatutUpdateDTO request) {
        return ResponseEntity.ok(reclamationService.updateStatut(id, request));
    }

    @GetMapping("/api/declarations/{declarationId}/reclamations")
    @Operation(summary = "Lister les réclamations d'une déclaration")
    public ResponseEntity<List<ReclamationResponseDTO>> getByDeclaration(@PathVariable Long declarationId) {
        return ResponseEntity.ok(reclamationService.getReclamationsByDeclaration(declarationId));
    }
}
