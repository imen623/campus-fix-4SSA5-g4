package tn.esprit.campusfix.intervention.control;

import tn.esprit.campusfix.intervention.domain.Code;

import tn.esprit.campusfix.intervention.service.CodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/codes")
@AllArgsConstructor
@Tag(name = "Gestion Code")
public class CodeRestController {
    private final CodeService service;

    @Operation(description = "Récuperer tous les codes")
    @GetMapping
    public List<Code> retrieveAll() {
        return service.retrieveAllCodes();
    }

    @Operation(description = "Récuperer un Code par son identifiant")
    @GetMapping("/{id}")
    public Code retrieve(@PathVariable Long id) {
        return service.retrieveCode(id);
    }

    @Operation(description = "Ajouter un Code")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Code add(@RequestBody Code valeur) {
        return service.addCode(valeur);
    }

    @Operation(description = "Modifier un Code")
    @PutMapping("/{id}")
    public Code modify(@PathVariable Long id, @RequestBody Code valeur) {
        return service.modifyCode(id, valeur);
    }

    @Operation(description = "Supprimer un Code")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        service.removeCode(id);
    }

}
