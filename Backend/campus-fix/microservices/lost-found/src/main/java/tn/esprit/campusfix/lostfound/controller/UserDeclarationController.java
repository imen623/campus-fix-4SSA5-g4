package tn.esprit.campusfix.lostfound.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.campusfix.lostfound.dto.response.DeclarationResponseDTO;
import tn.esprit.campusfix.lostfound.dto.response.ReclamationResponseDTO;
import tn.esprit.campusfix.lostfound.service.DeclarationService;
import tn.esprit.campusfix.lostfound.service.ReclamationService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Utilisateurs - Objets & Réclamations", description = "Consultation des déclarations et réclamations par utilisateur")
public class UserDeclarationController {

    private final DeclarationService declarationService;
    private final ReclamationService reclamationService;

    public UserDeclarationController(DeclarationService declarationService, ReclamationService reclamationService) {
        this.declarationService = declarationService;
        this.reclamationService = reclamationService;
    }

    @GetMapping("/{userId}/declarations")
    @Operation(summary = "Lister les déclarations d'un utilisateur")
    public ResponseEntity<List<DeclarationResponseDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(declarationService.getDeclarationsByUser(userId));
    }

    @GetMapping("/{userId}/reclamations")
    @Operation(summary = "Lister les réclamations d'un utilisateur")
    public ResponseEntity<List<ReclamationResponseDTO>> getReclamationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reclamationService.getReclamationsByUser(userId));
    }
}
