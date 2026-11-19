package tn.esprit.campusfix.intervention.control;

import tn.esprit.campusfix.intervention.domain.Intervention;
import tn.esprit.campusfix.intervention.domain.StatutIntervention;
import tn.esprit.campusfix.intervention.service.InterventionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/interventions")
@AllArgsConstructor
@Tag(name = "Gestion Intervention")
public class InterventionRestController {
    private final InterventionService service;

    @Operation(description = "Recuperer toutes les interventions")
    @GetMapping
    public List<Intervention> retrieveAll() {
        return service.retrieveAllInterventions();
    }

    @Operation(description = "Recuperer une intervention par son identifiant")
    @GetMapping("/{id}")
    public Intervention retrieve(@PathVariable Long id) {
        return service.retrieveIntervention(id);
    }

    @Operation(description = "Ajouter une intervention")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Intervention add(@RequestBody Intervention valeur) {
        return service.addIntervention(valeur);
    }

    @Operation(description = "Modifier une intervention")
    @PutMapping("/{id}")
    public Intervention modify(@PathVariable Long id, @RequestBody Intervention valeur) {
        return service.modifyIntervention(id, valeur);
    }

    @Operation(description = "Supprimer une intervention")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        service.removeIntervention(id);
    }

    @Operation(description = "Consulter les interventions d'un technicien")
    @GetMapping("/technicien/{technicienId}")
    public List<Intervention> parTechnicien(@PathVariable Long technicienId) {
        return service.retrieveByTechnicien(technicienId);
    }

    @Operation(description = "Consulter les interventions d'un signalement")
    @GetMapping("/signalement/{signalementId}")
    public List<Intervention> parSignalement(@PathVariable Long signalementId) {
        return service.retrieveBySignalement(signalementId);
    }

    @Operation(description = "Modifier le statut d'une intervention")
    @PatchMapping("/{id}/status")
    public Intervention changerStatut(@PathVariable Long id, @RequestParam StatutIntervention statut) {
        return service.changerStatut(id, statut);
    }

}
