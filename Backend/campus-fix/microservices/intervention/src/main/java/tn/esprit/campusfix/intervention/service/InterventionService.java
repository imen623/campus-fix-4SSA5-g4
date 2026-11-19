package tn.esprit.campusfix.intervention.service;
import tn.esprit.campusfix.intervention.domain.Intervention;
import tn.esprit.campusfix.intervention.domain.StatutIntervention;
import java.util.List;

public interface InterventionService {
    List<Intervention> retrieveAllInterventions();
    Intervention retrieveIntervention(Long id);
    Intervention addIntervention(Intervention valeur);
    Intervention modifyIntervention(Long id, Intervention valeur);
    void removeIntervention(Long id);

List<Intervention> retrieveByTechnicien(Long technicienId);
List<Intervention> retrieveBySignalement(Long signalementId);
Intervention changerStatut(Long id, StatutIntervention statut);

}
