package tn.esprit.campusfix.roomresource.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.campusfix.roomresource.enums.RessourceStatus;
import tn.esprit.campusfix.roomresource.enums.RessourceType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ressource")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "reservations")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ressource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RessourceType type;

    @Column(nullable = false)
    private String categorie;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String localisation;

    private Integer capacite;

    @Column(name = "code_inventaire")
    private String codeInventaire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RessourceStatus statut = RessourceStatus.ACTIVE;

    @ManyToMany(mappedBy = "ressources")
    @JsonIgnore
    @Builder.Default
    private Set<Reservation> reservations = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (this.statut == null) {
            this.statut = RessourceStatus.ACTIVE;
        }
    }
}
