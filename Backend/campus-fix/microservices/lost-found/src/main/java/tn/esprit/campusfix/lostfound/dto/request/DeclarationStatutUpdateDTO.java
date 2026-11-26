package tn.esprit.campusfix.lostfound.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.campusfix.lostfound.entity.StatutDeclaration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeclarationStatutUpdateDTO {

    @NotNull(message = "Le statut est obligatoire")
    private StatutDeclaration statut;
}
