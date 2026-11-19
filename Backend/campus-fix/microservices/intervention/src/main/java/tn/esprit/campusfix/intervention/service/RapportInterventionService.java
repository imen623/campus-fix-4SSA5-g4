package tn.esprit.campusfix.intervention.service;
import tn.esprit.campusfix.intervention.domain.RapportIntervention;

import java.util.List;

public interface RapportInterventionService {
    List<RapportIntervention> retrieveAllRapportsIntervention();
    RapportIntervention retrieveRapportIntervention(Long id);
    RapportIntervention addRapportIntervention(RapportIntervention valeur);
    RapportIntervention modifyRapportIntervention(Long id, RapportIntervention valeur);
    void removeRapportIntervention(Long id);

}
