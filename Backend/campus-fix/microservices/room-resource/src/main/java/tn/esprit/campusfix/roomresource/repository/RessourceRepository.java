package tn.esprit.campusfix.roomresource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.campusfix.roomresource.entity.Ressource;
import tn.esprit.campusfix.roomresource.enums.RessourceStatus;
import tn.esprit.campusfix.roomresource.enums.RessourceType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RessourceRepository extends JpaRepository<Ressource, Long> {

    // Recherche par statut (ex: ACTIVE, EN_MAINTENANCE)
    List<Ressource> findByStatut(RessourceStatus statut);

    // Recherche par type (SALLE ou EQUIPEMENT)
    List<Ressource> findByType(RessourceType type);

    // Recherche par type et statut
    List<Ressource> findByTypeAndStatut(RessourceType type, RessourceStatus statut);

    // Recherche par categorie
    List<Ressource> findByCategorie(String categorie);

    // Recherche multicriteres avec filtres
    List<Ressource> findByTypeAndCategorieAndLocalisation(RessourceType type, String categorie, String localisation);

    /**
     * Trouver toutes les ressources ACTIVES disponibles sur un intervalle [debut, fin].
     * Une ressource est disponible si elle n'a AUCUNE reservation CONFIRMEE qui chevauche la periode.
     */
    @Query("""
        SELECT r FROM Ressource r 
        WHERE r.statut = tn.esprit.campusfix.roomresource.enums.RessourceStatus.ACTIVE 
        AND r.id NOT IN (
            SELECT res.id FROM Reservation rev 
            JOIN rev.ressources res 
            WHERE rev.statut = tn.esprit.campusfix.roomresource.enums.ReservationStatus.CONFIRMEE 
            AND rev.dateDebut < :fin 
            AND rev.dateFin > :debut
        )
    """)
    List<Ressource> findAvailableRessources(
        @Param("debut") LocalDateTime debut, 
        @Param("fin") LocalDateTime fin
    );
}
