package tn.esprit.campusfix.intervention.control;

import tn.esprit.campusfix.intervention.domain.RapportIntervention;

import tn.esprit.campusfix.intervention.service.RapportInterventionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rapports")
@AllArgsConstructor
@Tag(name = "Gestion RapportIntervention")
public class RapportInterventionRestController {
    private final RapportInterventionService service;

    @Operation(description = "Recuperer tous les rapportsintervention")
    @GetMapping
    public List<RapportIntervention> retrieveAll() {
        return service.retrieveAllRapportsIntervention();
    }

    @Operation(description = "Recuperer un RapportIntervention par son identifiant")
    @GetMapping("/{id}")
    public RapportIntervention retrieve(@PathVariable Long id) {
        return service.retrieveRapportIntervention(id);
    }

    @Operation(description = "Ajouter un RapportIntervention")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RapportIntervention add(@RequestBody RapportIntervention valeur) {
        return service.addRapportIntervention(valeur);
    }

    @Operation(description = "Modifier un RapportIntervention")
    @PutMapping("/{id}")
    public RapportIntervention modify(@PathVariable Long id, @RequestBody RapportIntervention valeur) {
        return service.modifyRapportIntervention(id, valeur);
    }

    @Operation(description = "Supprimer un RapportIntervention")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        service.removeRapportIntervention(id);
    }

}
