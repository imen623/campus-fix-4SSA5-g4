package tn.esprit.campusfix.lostfound.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.campusfix.lostfound.entity.StatutReclamation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReclamationStatutUpdateDTO {

    @NotNull(message = "Le statut est obligatoire")
    private StatutReclamation statut;
}
