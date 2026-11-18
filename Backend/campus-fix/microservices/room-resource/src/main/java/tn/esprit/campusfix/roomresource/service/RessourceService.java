package tn.esprit.campusfix.roomresource.service;

import tn.esprit.campusfix.roomresource.dto.RessourceRequestDTO;
import tn.esprit.campusfix.roomresource.dto.RessourceResponseDTO;
import tn.esprit.campusfix.roomresource.enums.RessourceStatus;
import tn.esprit.campusfix.roomresource.enums.RessourceType;

import java.time.LocalDateTime;
import java.util.List;

public interface RessourceService {

    RessourceResponseDTO createRessource(RessourceRequestDTO dto);

    RessourceResponseDTO updateRessource(Long id, RessourceRequestDTO dto);

    void deleteRessource(Long id);

    RessourceResponseDTO getRessourceById(Long id);

    List<RessourceResponseDTO> getAllRessources();

    List<RessourceResponseDTO> getRessourcesByStatut(RessourceStatus statut);

    List<RessourceResponseDTO> getRessourcesByType(RessourceType type);

    List<RessourceResponseDTO> filterRessources(RessourceType type, String categorie, String localisation);

    RessourceResponseDTO updateStatut(Long id, RessourceStatus newStatut);

    List<RessourceResponseDTO> getAvailableRessources(LocalDateTime debut, LocalDateTime fin);
}
