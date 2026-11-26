package tn.esprit.campusfix.lostfound.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.campusfix.lostfound.entity.Reclamation;
import tn.esprit.campusfix.lostfound.entity.StatutReclamation;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {

    List<Reclamation> findByDeclarationId(Long declarationId);

    List<Reclamation> findByUserId(Long userId);

    Optional<Reclamation> findByDeclarationIdAndUserId(Long declarationId, Long userId);

    boolean existsByDeclarationIdAndStatut(Long declarationId, StatutReclamation statut);

    List<Reclamation> findByDeclarationIdAndStatut(Long declarationId, StatutReclamation statut);
}
