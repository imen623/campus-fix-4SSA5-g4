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
public class RapportInterventionServiceImpl implements RapportInterventionService {
    private final RapportInterventionRepository rapportRepository;
    private final InterventionRepository interventionRepository;

    @Override
    public List<RapportIntervention> retrieveAllRapportsIntervention() {
        return rapportRepository.findAll();
    }

    @Override
    public RapportIntervention retrieveRapportIntervention(Long id) {
        return rapportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RapportIntervention introuvable"));
    }

    @Override
    @Transactional
    public RapportIntervention addRapportIntervention(RapportIntervention valeur) {
        valeur.setId(null);

        valeur.setIntervention(trouverIntervention(valeur.getIntervention()));
        if (rapportRepository.existsByIntervention_Id(valeur.getIntervention().getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un rapport existe deja pour cette intervention");
        }

        return rapportRepository.save(valeur);
    }

    @Override
    @Transactional
    public RapportIntervention modifyRapportIntervention(Long id, RapportIntervention valeur) {
        RapportIntervention existant = retrieveRapportIntervention(id);
        Intervention intervention = trouverIntervention(valeur.getIntervention());
        rapportRepository.findByIntervention_Id(intervention.getId()).ifPresent(rapport -> {
            if (!rapport.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Un rapport existe deja pour cette intervention");
            }
        });
        existant.setIntervention(intervention);
        existant.setDateRapport(valeur.getDateRapport());
        existant.setTravauxEffectues(valeur.getTravauxEffectues());
        existant.setResultat(valeur.getResultat());
        existant.setRemarques(valeur.getRemarques());
        return rapportRepository.save(existant);
    }

    @Override
    @Transactional
    public void removeRapportIntervention(Long id) {
        retrieveRapportIntervention(id);

        rapportRepository.deleteById(id);
    }


    private Intervention trouverIntervention(Intervention reference) {
        if (reference == null || reference.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "intervention.id obligatoire");
        }
        return interventionRepository.findById(reference.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Intervention introuvable"));
    }

}
