package tn.esprit.campusfix.intervention.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "interventions")
@Getter
@Setter
@NoArgsConstructor
public class Intervention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private LocalDateTime datePlanifiee;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutIntervention statut = StatutIntervention.PLANIFIEE;

    @Column(nullable = false, length = 2000)
    private String description;

    // Identifiants externes : pas de relation JPA vers les autres microservices
    @Column(nullable = false)
    private Long signalementId;

    @Column(nullable = false)
    private Long equipementId;

    @Column(nullable = false)
    private Long demandeurId;

    @Column(nullable = false)
    private Long technicienAffecteId;

    @Column(updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateMiseAJour;

}
