package tn.esprit.campusfix.intervention.repository;
import tn.esprit.campusfix.intervention.domain.ElementInspection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementInspectionRepository extends JpaRepository<ElementInspection, Long> {
    boolean existsByInspection_Id(Long inspectionId);
    boolean existsByElement_Id(Long elementId);
    boolean existsByCode_Id(Long codeId);
}
