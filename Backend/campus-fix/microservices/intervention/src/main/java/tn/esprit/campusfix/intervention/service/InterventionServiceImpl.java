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
public class InterventionServiceImpl implements InterventionService {
    private final InterventionRepository interventionRepository;
    private final InspectionRepository inspectionRepository;
    private final RapportInterventionRepository rapportRepository;

    @Override
    public List<Intervention> retrieveAllInterventions() {
        return interventionRepository.findAll();
    }

    @Override
    public Intervention retrieveIntervention(Long id) {
        return interventionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Intervention introuvable"));
    }

    @Override
    @Transactional
    public Intervention addIntervention(Intervention valeur) {
        valeur.setId(null);
        valeur.setDateCreation(LocalDateTime.now());
        valeur.setDateMiseAJour(LocalDateTime.now());

        if (valeur.getStatut() == null) valeur.setStatut(StatutIntervention.PLANIFIEE);
        verifierDates(valeur);

        return interventionRepository.save(valeur);
    }

    @Override
    @Transactional
    public Intervention modifyIntervention(Long id, Intervention valeur) {
        Intervention existant = retrieveIntervention(id);
        existant.setDateMiseAJour(LocalDateTime.now());
        existant.setDatePlanifiee(valeur.getDatePlanifiee());
        existant.setDateDebut(valeur.getDateDebut());
        existant.setDateFin(valeur.getDateFin());
        existant.setStatut(valeur.getStatut() == null ? existant.getStatut() : valeur.getStatut());
        existant.setDescription(valeur.getDescription());
        existant.setSignalementId(valeur.getSignalementId());
        existant.setEquipementId(valeur.getEquipementId());
        existant.setDemandeurId(valeur.getDemandeurId());
        existant.setTechnicienAffecteId(valeur.getTechnicienAffecteId());
        verifierDates(existant);
        return interventionRepository.save(existant);
    }

    @Override
    @Transactional
    public void removeIntervention(Long id) {
        retrieveIntervention(id);

        if (inspectionRepository.existsByIntervention_Id(id) || rapportRepository.existsByIntervention_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Intervention liee a des inspections ou a un rapport");
        }

        interventionRepository.deleteById(id);
    }

    @Override
    public List<Intervention> retrieveByTechnicien(Long technicienId) {
        return interventionRepository.findByTechnicienAffecteId(technicienId);
    }

    @Override
    public List<Intervention> retrieveBySignalement(Long signalementId) {
        return interventionRepository.findBySignalementId(signalementId);
    }

    @Override
    public Intervention changerStatut(Long id, StatutIntervention statut) {
        if (statut == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Statut obligatoire");
        }
        Intervention existant = retrieveIntervention(id);
        existant.setDateMiseAJour(LocalDateTime.now());
        existant.setStatut(statut);
        return interventionRepository.save(existant);
    }


    private void verifierDates(Intervention intervention) {
        if (intervention.getDateDebut() != null && intervention.getDateFin() != null
                && intervention.getDateFin().isBefore(intervention.getDateDebut())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La date de fin doit etre apres la date de debut");
        }
    }

}
