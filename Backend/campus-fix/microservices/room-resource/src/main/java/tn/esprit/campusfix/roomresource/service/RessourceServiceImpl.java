package tn.esprit.campusfix.roomresource.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.campusfix.roomresource.dto.RessourceRequestDTO;
import tn.esprit.campusfix.roomresource.dto.RessourceResponseDTO;
import tn.esprit.campusfix.roomresource.entity.Ressource;
import tn.esprit.campusfix.roomresource.enums.RessourceStatus;
import tn.esprit.campusfix.roomresource.enums.RessourceType;
import tn.esprit.campusfix.roomresource.exception.InvalidPeriodException;
import tn.esprit.campusfix.roomresource.exception.ResourceNotFoundException;
import tn.esprit.campusfix.roomresource.repository.RessourceRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RessourceServiceImpl implements RessourceService {

    private final RessourceRepository ressourceRepository;

    @Override
    public RessourceResponseDTO createRessource(RessourceRequestDTO dto) {
        Ressource ressource = Ressource.builder()
                .nom(dto.getNom())
                .type(dto.getType())
                .categorie(dto.getCategorie())
                .description(dto.getDescription())
                .localisation(dto.getLocalisation())
                .capacite(dto.getCapacite())
                .codeInventaire(dto.getCodeInventaire())
                .statut(dto.getStatut() != null ? dto.getStatut() : RessourceStatus.ACTIVE)
                .build();

        Ressource saved = ressourceRepository.save(ressource);
        return mapToResponse(saved);
    }

    @Override
    public RessourceResponseDTO updateRessource(Long id, RessourceRequestDTO dto) {
        Ressource ressource = findEntityById(id);

        ressource.setNom(dto.getNom());
        ressource.setType(dto.getType());
        ressource.setCategorie(dto.getCategorie());
        ressource.setDescription(dto.getDescription());
        ressource.setLocalisation(dto.getLocalisation());
        ressource.setCapacite(dto.getCapacite());
        ressource.setCodeInventaire(dto.getCodeInventaire());
        if (dto.getStatut() != null) {
            ressource.setStatut(dto.getStatut());
        }

        Ressource updated = ressourceRepository.save(ressource);
        return mapToResponse(updated);
    }

    @Override
    public void deleteRessource(Long id) {
        Ressource ressource = findEntityById(id);
        ressourceRepository.delete(ressource);
    }

    @Override
    @Transactional(readOnly = true)
    public RessourceResponseDTO getRessourceById(Long id) {
        return mapToResponse(findEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RessourceResponseDTO> getAllRessources() {
        return ressourceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RessourceResponseDTO> getRessourcesByStatut(RessourceStatus statut) {
        return ressourceRepository.findByStatut(statut)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RessourceResponseDTO> getRessourcesByType(RessourceType type) {
        return ressourceRepository.findByType(type)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RessourceResponseDTO> filterRessources(RessourceType type, String categorie, String localisation) {
        return ressourceRepository.findByTypeAndCategorieAndLocalisation(type, categorie, localisation)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public RessourceResponseDTO updateStatut(Long id, RessourceStatus newStatut) {
        Ressource ressource = findEntityById(id);
        ressource.setStatut(newStatut);
        return mapToResponse(ressourceRepository.save(ressource));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RessourceResponseDTO> getAvailableRessources(LocalDateTime debut, LocalDateTime fin) {
        if (debut == null || fin == null || !debut.isBefore(fin)) {
            throw new InvalidPeriodException("La date de debut doit etre anterieure a la date de fin");
        }
        return ressourceRepository.findAvailableRessources(debut, fin)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private Ressource findEntityById(Long id) {
        return ressourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ressource introuvable avec l'identifiant : " + id));
    }

    public RessourceResponseDTO mapToResponse(Ressource entity) {
        return RessourceResponseDTO.builder()
                .id(entity.getId())
                .nom(entity.getNom())
                .type(entity.getType())
                .categorie(entity.getCategorie())
                .description(entity.getDescription())
                .localisation(entity.getLocalisation())
                .capacite(entity.getCapacite())
                .codeInventaire(entity.getCodeInventaire())
                .statut(entity.getStatut())
                .build();
    }
}
