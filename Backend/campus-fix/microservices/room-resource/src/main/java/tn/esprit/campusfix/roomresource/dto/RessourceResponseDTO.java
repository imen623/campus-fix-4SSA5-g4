package tn.esprit.campusfix.roomresource.dto;

import lombok.*;
import tn.esprit.campusfix.roomresource.enums.RessourceStatus;
import tn.esprit.campusfix.roomresource.enums.RessourceType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RessourceResponseDTO {

    private Long id;
    private String nom;
    private RessourceType type;
    private String categorie;
    private String description;
    private String localisation;
    private Integer capacite;
    private String codeInventaire;
    private RessourceStatus statut;
}
