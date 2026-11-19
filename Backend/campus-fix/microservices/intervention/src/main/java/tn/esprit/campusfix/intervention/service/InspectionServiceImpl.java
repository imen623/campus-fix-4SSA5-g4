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
public class InspectionServiceImpl implements InspectionService {
    private final InspectionRepository inspectionRepository;
    private final InterventionRepository interventionRepository;
    private final ElementInspectionRepository elementInspectionRepository;

    @Override
    public List<Inspection> retrieveAllInspections() {
        return inspectionRepository.findAll();
    }

    @Override
    public Inspection retrieveInspection(Long id) {
        return inspectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inspection introuvable"));
    }

    @Override
    @Transactional
    public Inspection addInspection(Inspection valeur) {
        valeur.setId(null);
        valeur.setDateCreation(LocalDateTime.now());
        valeur.setDateMiseAJour(LocalDateTime.now());

        valeur.setIntervention(trouverIntervention(valeur.getIntervention()));

        return inspectionRepository.save(valeur);
    }

    @Override
    @Transactional
    public Inspection modifyInspection(Long id, Inspection valeur) {
        Inspection existant = retrieveInspection(id);
        existant.setDateMiseAJour(LocalDateTime.now());
        existant.setDateInspection(valeur.getDateInspection());
        existant.setDescription(valeur.getDescription());
        existant.setResultat(valeur.getResultat());
        existant.setIntervention(trouverIntervention(valeur.getIntervention()));
        return inspectionRepository.save(existant);
    }

    @Override
    @Transactional
    public void removeInspection(Long id) {
        retrieveInspection(id);

        if (elementInspectionRepository.existsByInspection_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Inspection liee a des elements d'inspection");
        }

        inspectionRepository.deleteById(id);
    }


    private Intervention trouverIntervention(Intervention reference) {
        if (reference == null || reference.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "intervention.id obligatoire");
        }
        return interventionRepository.findById(reference.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Intervention introuvable"));
    }

}
