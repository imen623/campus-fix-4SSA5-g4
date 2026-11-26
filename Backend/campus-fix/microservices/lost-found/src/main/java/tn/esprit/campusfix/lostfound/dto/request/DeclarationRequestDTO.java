package tn.esprit.campusfix.lostfound.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import tn.esprit.campusfix.lostfound.entity.CategorieObjet;
import tn.esprit.campusfix.lostfound.entity.TypeDeclaration;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeclarationRequestDTO {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 150, message = "Le titre ne peut pas dépasser 150 caractères")
    private String titre;

    private String description;

    @NotNull(message = "La catégorie est obligatoire")
    private CategorieObjet categorie;

    @NotNull(message = "Le type de déclaration est obligatoire (PERDU ou TROUVE)")
    private TypeDeclaration typeDeclaration;

    @NotNull(message = "L'identifiant de l'utilisateur est obligatoire")
    private Long userId;

    private Long salleId;

    private Long eventId;

    @Size(max = 200, message = "Le lieu ne peut pas dépasser 200 caractères")
    private String lieu;

    private LocalDate dateEvenement;

    @Size(max = 500, message = "L'URL de l'image ne peut pas dépasser 500 caractères")
    private String imageUrl;
}
