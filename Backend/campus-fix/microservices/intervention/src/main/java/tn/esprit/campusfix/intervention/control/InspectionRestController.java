package tn.esprit.campusfix.intervention.control;

import tn.esprit.campusfix.intervention.domain.Inspection;

import tn.esprit.campusfix.intervention.service.InspectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inspections")
@AllArgsConstructor
@Tag(name = "Gestion Inspection")
public class InspectionRestController {
    private final InspectionService service;

    @Operation(description = "Recuperer tous les inspections")
    @GetMapping
    public List<Inspection> retrieveAll() {
        return service.retrieveAllInspections();
    }

    @Operation(description = "Recuperer un Inspection par son identifiant")
    @GetMapping("/{id}")
    public Inspection retrieve(@PathVariable Long id) {
        return service.retrieveInspection(id);
    }

    @Operation(description = "Ajouter un Inspection")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Inspection add(@RequestBody Inspection valeur) {
        return service.addInspection(valeur);
    }

    @Operation(description = "Modifier un Inspection")
    @PutMapping("/{id}")
    public Inspection modify(@PathVariable Long id, @RequestBody Inspection valeur) {
        return service.modifyInspection(id, valeur);
    }

    @Operation(description = "Supprimer un Inspection")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        service.removeInspection(id);
    }

}
