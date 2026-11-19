package tn.esprit.campusfix.intervention.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inspections")
@Getter
@Setter
@NoArgsConstructor
public class Inspection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private LocalDateTime dateInspection;

    @Column(nullable = false, length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultatInspection resultat;

    @ManyToOne(optional = false)
    @JoinColumn(name = "intervention_id", nullable = false)
    private Intervention intervention;

    @Column(updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateMiseAJour;

}
