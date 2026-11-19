package tn.esprit.campusfix.intervention.control;

import tn.esprit.campusfix.intervention.domain.ElementInspection;

import tn.esprit.campusfix.intervention.service.ElementInspectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/elements-inspection")
@AllArgsConstructor
@Tag(name = "Gestion ElementInspection")
public class ElementInspectionRestController {
    private final ElementInspectionService service;

    @Operation(description = "Recuperer tous les elementsinspection")
    @GetMapping
    public List<ElementInspection> retrieveAll() {
        return service.retrieveAllElementsInspection();
    }

    @Operation(description = "Recuperer un ElementInspection par son identifiant")
    @GetMapping("/{id}")
    public ElementInspection retrieve(@PathVariable Long id) {
        return service.retrieveElementInspection(id);
    }

    @Operation(description = "Ajouter un ElementInspection")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ElementInspection add(@RequestBody ElementInspection valeur) {
        return service.addElementInspection(valeur);
    }

    @Operation(description = "Modifier un ElementInspection")
    @PutMapping("/{id}")
    public ElementInspection modify(@PathVariable Long id, @RequestBody ElementInspection valeur) {
        return service.modifyElementInspection(id, valeur);
    }

    @Operation(description = "Supprimer un ElementInspection")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        service.removeElementInspection(id);
    }

}
