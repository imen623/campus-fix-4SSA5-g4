package tn.esprit.campusfix.intervention.service;

import tn.esprit.campusfix.intervention.domain.*;
import tn.esprit.campusfix.intervention.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ElementServiceImpl implements ElementService {
    private final ElementRepository elementRepository;
    private final ElementInspectionRepository elementInspectionRepository;

    @Override
    public List<Element> retrieveAllElements() {
        return elementRepository.findAll();
    }

    @Override
    public Element retrieveElement(Long id) {
        return elementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Element introuvable"));
    }

    @Override
    @Transactional
    public Element addElement(Element valeur) {
        valeur.setId(null);
        valeur.setDateCreation(LocalDateTime.now());
        valeur.setDateMiseAJour(LocalDateTime.now());

        return elementRepository.save(valeur);
    }

    @Override
    @Transactional
    public Element modifyElement(Long id, Element valeur) {
        Element existant = retrieveElement(id);
        existant.setDateMiseAJour(LocalDateTime.now());
        existant.setDescription(valeur.getDescription());
        return elementRepository.save(existant);
    }

    @Override
    @Transactional
    public void removeElement(Long id) {
        retrieveElement(id);

        if (elementInspectionRepository.existsByElement_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Element utilise dans une inspection");
        }

        elementRepository.deleteById(id);
    }


}
