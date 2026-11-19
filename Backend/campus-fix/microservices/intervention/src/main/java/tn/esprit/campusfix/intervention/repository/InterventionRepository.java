package tn.esprit.campusfix.intervention.repository;
import tn.esprit.campusfix.intervention.domain.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterventionRepository extends JpaRepository<Intervention, Long> {
    List<Intervention> findByTechnicienAffecteId(Long technicienId);
    List<Intervention> findBySignalementId(Long signalementId);
}
