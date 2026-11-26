package tn.esprit.campusfix.lostfound.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReclamationRequestDTO {

    @NotNull(message = "L'identifiant de la déclaration est obligatoire")
    private Long declarationId;

    @NotNull(message = "L'identifiant de l'utilisateur est obligatoire")
    private Long userId;

    private String message;
}
