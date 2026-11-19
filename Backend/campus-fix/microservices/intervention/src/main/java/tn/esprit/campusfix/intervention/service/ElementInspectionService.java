package tn.esprit.campusfix.intervention.service;
import tn.esprit.campusfix.intervention.domain.ElementInspection;

import java.util.List;

public interface ElementInspectionService {
    List<ElementInspection> retrieveAllElementsInspection();
    ElementInspection retrieveElementInspection(Long id);
    ElementInspection addElementInspection(ElementInspection valeur);
    ElementInspection modifyElementInspection(Long id, ElementInspection valeur);
    void removeElementInspection(Long id);

}
