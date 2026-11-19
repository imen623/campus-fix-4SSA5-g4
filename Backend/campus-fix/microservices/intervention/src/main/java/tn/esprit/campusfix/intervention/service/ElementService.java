package tn.esprit.campusfix.intervention.service;
import tn.esprit.campusfix.intervention.domain.Element;

import java.util.List;

public interface ElementService {
    List<Element> retrieveAllElements();
    Element retrieveElement(Long id);
    Element addElement(Element valeur);
    Element modifyElement(Long id, Element valeur);
    void removeElement(Long id);

}
