package tn.esprit.campusfix.roomresource.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import tn.esprit.campusfix.roomresource.enums.RessourceStatus;
import tn.esprit.campusfix.roomresource.enums.RessourceType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RessourceRequestDTO {

    @NotBlank(message = "Le nom de la ressource est obligatoire")
    private String nom;

    @NotNull(message = "Le type (SALLE ou EQUIPEMENT) est obligatoire")
    private RessourceType type;

    @NotBlank(message = "La categorie est obligatoire")
    private String categorie;

    private String description;

    @NotBlank(message = "La localisation est obligatoire")
    private String localisation;

    @Positive(message = "La capacite doit etre un nombre strictement positif")
    private Integer capacite;

    private String codeInventaire;

    private RessourceStatus statut;
}
