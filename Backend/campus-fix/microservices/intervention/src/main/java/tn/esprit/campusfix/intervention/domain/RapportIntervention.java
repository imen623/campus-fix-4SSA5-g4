package tn.esprit.campusfix.intervention.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "rapports_intervention")
@Getter
@Setter
@NoArgsConstructor
public class RapportIntervention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private LocalDateTime dateRapport;

    @Column(nullable = false, length = 2000)
    private String travauxEffectues;

    @Column(nullable = false, length = 2000)
    private String resultat;

    @Column(length = 2000)
    private String remarques;

    @OneToOne(optional = false)
    @JoinColumn(name = "intervention_id", nullable = false, unique = true)
    private Intervention intervention;


}
