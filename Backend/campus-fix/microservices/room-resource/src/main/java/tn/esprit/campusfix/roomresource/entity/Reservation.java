package tn.esprit.campusfix.roomresource.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.campusfix.roomresource.enums.ReservationStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "ressources")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDateTime dateFin;

    @Column(nullable = false)
    private String motif;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ReservationStatus statut = ReservationStatus.EN_ATTENTE;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "event_id")
    private Long eventId;

    @ManyToMany
    @JoinTable(
        name = "reservation_ressource",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "ressource_id")
    )
    @Builder.Default
    private Set<Ressource> ressources = new HashSet<>();

    /**
     * Methode d'assistance pour synchroniser la relation bidirectionnelle en memoire.
     */
    public void addRessource(Ressource ressource) {
        if (ressource != null) {
            this.ressources.add(ressource);
            ressource.getReservations().add(this);
        }
    }

    /**
     * Methode d'assistance pour retirer proprement une ressource de la relation.
     */
    public void removeRessource(Ressource ressource) {
        if (ressource != null) {
            this.ressources.remove(ressource);
            ressource.getReservations().remove(this);
        }
    }

    @PrePersist
    public void prePersist() {
        if (this.dateCreation == null) {
            this.dateCreation = LocalDateTime.now();
        }
        if (this.statut == null) {
            this.statut = ReservationStatus.EN_ATTENTE;
        }
    }
}
