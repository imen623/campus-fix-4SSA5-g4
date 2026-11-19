package tn.esprit.campusfix.intervention.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "codes")
@Getter
@Setter
@NoArgsConstructor
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String valeur;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateMiseAJour;

}
