package tn.esprit.campusfix.intervention.repository;
import tn.esprit.campusfix.intervention.domain.RapportIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RapportInterventionRepository extends JpaRepository<RapportIntervention, Long> {
    boolean existsByIntervention_Id(Long interventionId);
    Optional<RapportIntervention> findByIntervention_Id(Long interventionId);
}
