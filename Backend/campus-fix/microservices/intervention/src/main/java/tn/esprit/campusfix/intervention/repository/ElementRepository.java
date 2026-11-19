package tn.esprit.campusfix.intervention.repository;
import tn.esprit.campusfix.intervention.domain.Element;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementRepository extends JpaRepository<Element, Long> {
}
