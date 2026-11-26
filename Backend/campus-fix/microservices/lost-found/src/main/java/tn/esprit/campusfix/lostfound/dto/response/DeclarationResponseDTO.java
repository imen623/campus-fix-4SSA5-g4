package tn.esprit.campusfix.lostfound.dto.response;

import lombok.*;
import tn.esprit.campusfix.lostfound.entity.CategorieObjet;
import tn.esprit.campusfix.lostfound.entity.StatutDeclaration;
import tn.esprit.campusfix.lostfound.entity.TypeDeclaration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeclarationResponseDTO {
    private Long id;
    private String titre;
    private String description;
    private CategorieObjet categorie;
    private TypeDeclaration typeDeclaration;
    private StatutDeclaration statut;
    private Long userId;
    private String declarantNom;
    private String declarantEmail;
    private Long salleId;
    private String salleNom;
    private Long eventId;
    private String eventTitre;
    private String lieu;
    private LocalDate dateEvenement;
    private String imageUrl;
    private int nbReclamations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
