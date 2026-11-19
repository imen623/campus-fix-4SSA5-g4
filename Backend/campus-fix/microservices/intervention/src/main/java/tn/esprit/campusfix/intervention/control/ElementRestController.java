package tn.esprit.campusfix.intervention.control;

import tn.esprit.campusfix.intervention.domain.Element;

import tn.esprit.campusfix.intervention.service.ElementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/elements")
@AllArgsConstructor
@Tag(name = "Gestion Element")
public class ElementRestController {
    private final ElementService service;

    @Operation(description = "Recuperer tous les elements")
    @GetMapping
    public List<Element> retrieveAll() {
        return service.retrieveAllElements();
    }

    @Operation(description = "Recuperer un Element par son identifiant")
    @GetMapping("/{id}")
    public Element retrieve(@PathVariable Long id) {
        return service.retrieveElement(id);
    }

    @Operation(description = "Ajouter un Element")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Element add(@RequestBody Element valeur) {
        return service.addElement(valeur);
    }

    @Operation(description = "Modifier un Element")
    @PutMapping("/{id}")
    public Element modify(@PathVariable Long id, @RequestBody Element valeur) {
        return service.modifyElement(id, valeur);
    }

    @Operation(description = "Supprimer un Element")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        service.removeElement(id);
    }

}
