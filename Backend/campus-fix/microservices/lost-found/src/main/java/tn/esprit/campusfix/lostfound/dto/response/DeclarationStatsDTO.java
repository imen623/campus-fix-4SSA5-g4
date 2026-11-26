package tn.esprit.campusfix.lostfound.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeclarationStatsDTO {
    private Long declarationId;
    private String titre;
    private int totalReclamations;
    private int reclamationsEnAttente;
    private int reclamationsAcceptees;
    private int reclamationsRejetees;
    private boolean estResolu;
}
