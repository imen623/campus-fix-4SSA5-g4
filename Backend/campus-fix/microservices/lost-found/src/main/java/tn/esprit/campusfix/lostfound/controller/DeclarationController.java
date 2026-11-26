package tn.esprit.campusfix.lostfound.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.campusfix.lostfound.dto.request.DeclarationRequestDTO;
import tn.esprit.campusfix.lostfound.dto.request.DeclarationStatutUpdateDTO;
import tn.esprit.campusfix.lostfound.dto.response.DeclarationResponseDTO;
import tn.esprit.campusfix.lostfound.dto.response.DeclarationStatsDTO;
import tn.esprit.campusfix.lostfound.entity.CategorieObjet;
import tn.esprit.campusfix.lostfound.entity.StatutDeclaration;
import tn.esprit.campusfix.lostfound.entity.TypeDeclaration;
import tn.esprit.campusfix.lostfound.service.DeclarationService;

@RestController
@RequestMapping("/api/declarations")
@Tag(name = "Déclarations", description = "Gestion des déclarations d'objets perdus et trouvés")
public class DeclarationController {

    private final DeclarationService declarationService;

    public DeclarationController(DeclarationService declarationService) {
        this.declarationService = declarationService;
    }

    @PostMapping
    @Operation(summary = "Créer une déclaration (objet perdu ou trouvé)")
    public ResponseEntity<DeclarationResponseDTO> create(@Valid @RequestBody DeclarationRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(declarationService.createDeclaration(request));
    }

    @GetMapping
    @Operation(summary = "Lister toutes les déclarations avec filtres et pagination")
    public ResponseEntity<Page<DeclarationResponseDTO>> getAll(
            @RequestParam(required = false) TypeDeclaration type,
            @RequestParam(required = false) CategorieObjet categorie,
            @RequestParam(required = false) StatutDeclaration statut,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(declarationService.getAllDeclarations(type, categorie, statut, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une déclaration par ID")
    public ResponseEntity<DeclarationResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(declarationService.getDeclarationById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une déclaration")
    public ResponseEntity<DeclarationResponseDTO> update(@PathVariable Long id,
                                                          @Valid @RequestBody DeclarationRequestDTO request) {
        return ResponseEntity.ok(declarationService.updateDeclaration(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une déclaration")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        declarationService.deleteDeclaration(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/statut")
    @Operation(summary = "Mettre à jour le statut d'une déclaration")
    public ResponseEntity<DeclarationResponseDTO> updateStatut(@PathVariable Long id,
                                                                @Valid @RequestBody DeclarationStatutUpdateDTO request) {
        return ResponseEntity.ok(declarationService.updateStatut(id, request));
    }

    @GetMapping("/{id}/stats")
    @Operation(summary = "Statistiques d'une déclaration (nombre de réclamations, statut)")
    public ResponseEntity<DeclarationStatsDTO> getStats(@PathVariable Long id) {
        return ResponseEntity.ok(declarationService.getStats(id));
    }
}
