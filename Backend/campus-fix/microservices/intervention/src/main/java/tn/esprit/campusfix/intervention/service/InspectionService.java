package tn.esprit.campusfix.intervention.service;
import tn.esprit.campusfix.intervention.domain.Inspection;

import java.util.List;

public interface InspectionService {
    List<Inspection> retrieveAllInspections();
    Inspection retrieveInspection(Long id);
    Inspection addInspection(Inspection valeur);
    Inspection modifyInspection(Long id, Inspection valeur);
    void removeInspection(Long id);

}
