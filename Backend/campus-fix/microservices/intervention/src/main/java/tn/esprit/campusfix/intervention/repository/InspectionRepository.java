package tn.esprit.campusfix.intervention.repository;
import tn.esprit.campusfix.intervention.domain.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InspectionRepository extends JpaRepository<Inspection, Long> {
    boolean existsByIntervention_Id(Long interventionId);
    List<Inspection> findByIntervention_Id(Long interventionId);
}
