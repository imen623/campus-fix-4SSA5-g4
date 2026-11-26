package tn.esprit.campusfix.lostfound.dto.response;

import lombok.*;
import tn.esprit.campusfix.lostfound.entity.StatutReclamation;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReclamationResponseDTO {
    private Long id;
    private Long declarationId;
    private String declarationTitre;
    private Long userId;
    private String reclamantNom;
    private String reclamantEmail;
    private String message;
    private StatutReclamation statut;
    private LocalDateTime dateReclamation;
    private LocalDateTime updatedAt;
}
