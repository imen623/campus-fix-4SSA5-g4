package tn.esprit.campusfix.intervention.service;

import tn.esprit.campusfix.intervention.domain.*;
import tn.esprit.campusfix.intervention.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class ElementInspectionServiceImpl implements ElementInspectionService {
    private final ElementInspectionRepository elementInspectionRepository;
    private final InspectionRepository inspectionRepository;
    private final ElementRepository elementRepository;
    private final CodeRepository codeRepository;

    @Override
    public List<ElementInspection> retrieveAllElementsInspection() {
        return elementInspectionRepository.findAll();
    }

    @Override
    public ElementInspection retrieveElementInspection(Long id) {
        return elementInspectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ElementInspection introuvable"));
    }

    @Override
    @Transactional
    public ElementInspection addElementInspection(ElementInspection valeur) {
        valeur.setId(null);

        valeur.setInspection(trouverInspection(valeur.getInspection()));
        valeur.setElement(trouverElement(valeur.getElement()));
        valeur.setCode(trouverCode(valeur.getCode()));

        return elementInspectionRepository.save(valeur);
    }

    @Override
    @Transactional
    public ElementInspection modifyElementInspection(Long id, ElementInspection valeur) {
        ElementInspection existant = retrieveElementInspection(id);
        existant.setValeur(valeur.getValeur());
        existant.setConformite(valeur.getConformite());
        existant.setRemarque(valeur.getRemarque());
        existant.setInspection(trouverInspection(valeur.getInspection()));
        existant.setElement(trouverElement(valeur.getElement()));
        existant.setCode(trouverCode(valeur.getCode()));
        return elementInspectionRepository.save(existant);
    }

    @Override
    @Transactional
    public void removeElementInspection(Long id) {
        retrieveElementInspection(id);

        elementInspectionRepository.deleteById(id);
    }


    private Inspection trouverInspection(Inspection reference) {
        if (reference == null || reference.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "inspection.id obligatoire");
        }
        return inspectionRepository.findById(reference.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inspection introuvable"));
    }
    private Element trouverElement(Element reference) {
        if (reference == null || reference.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "element.id obligatoire");
        }
        return elementRepository.findById(reference.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Element introuvable"));
    }
    private Code trouverCode(Code reference) {
        if (reference == null || reference.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "code.id obligatoire");
        }
        return codeRepository.findById(reference.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Code introuvable"));
    }

}
