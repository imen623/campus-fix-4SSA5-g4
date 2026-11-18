package tn.esprit.campusfix.roomresource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.campusfix.roomresource.entity.Reservation;
import tn.esprit.campusfix.roomresource.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Recherche de toutes les reservations d'un utilisateur
    List<Reservation> findByUserId(Long userId);

    // Recherche des reservations associees a un evenement
    List<Reservation> findByEventId(Long eventId);

    // Recherche par statut (ex: EN_ATTENTE, CONFIRMEE)
    List<Reservation> findByStatut(ReservationStatus statut);

    /**
     * Detection des conflits de reservation :
     * Trouve s'il existe deja une reservation CONFIRMEE pour la ressource donnee
     * qui chevauche la plage [debut, fin].
     * 
     * Formule mathematique du chevauchement :
     * (r.dateDebut < :fin) ET (r.dateFin > :debut)
     */
    @Query("""
        SELECT r FROM Reservation r 
        JOIN r.ressources res 
        WHERE res.id = :ressourceId 
        AND r.statut = tn.esprit.campusfix.roomresource.enums.ReservationStatus.CONFIRMEE 
        AND r.dateDebut < :fin 
        AND r.dateFin > :debut
    """)
    List<Reservation> findOverlappingConfirmedReservations(
        @Param("ressourceId") Long ressourceId,
        @Param("debut") LocalDateTime debut,
        @Param("fin") LocalDateTime fin
    );

    /**
     * Detection de conflits lors d'une mise a jour (on exclut la reservation courante pour ne pas s'auto-bloquer).
     */
    @Query("""
        SELECT r FROM Reservation r 
        JOIN r.ressources res 
        WHERE res.id = :ressourceId 
        AND r.id <> :reservationId 
        AND r.statut = tn.esprit.campusfix.roomresource.enums.ReservationStatus.CONFIRMEE 
        AND r.dateDebut < :fin 
        AND r.dateFin > :debut
    """)
    List<Reservation> findOverlappingConfirmedReservationsExcludingId(
        @Param("ressourceId") Long ressourceId,
        @Param("reservationId") Long reservationId,
        @Param("debut") LocalDateTime debut,
        @Param("fin") LocalDateTime fin
    );
}
