package tn.esprit.campusfix.lostfound.service;

import tn.esprit.campusfix.lostfound.dto.request.ReclamationRequestDTO;
import tn.esprit.campusfix.lostfound.dto.request.ReclamationStatutUpdateDTO;
import tn.esprit.campusfix.lostfound.dto.response.ReclamationResponseDTO;

import java.util.List;

public interface ReclamationService {

    ReclamationResponseDTO createReclamation(ReclamationRequestDTO request);

    ReclamationResponseDTO getReclamationById(Long id);

    ReclamationResponseDTO updateStatut(Long id, ReclamationStatutUpdateDTO request);

    List<ReclamationResponseDTO> getReclamationsByDeclaration(Long declarationId);

    List<ReclamationResponseDTO> getReclamationsByUser(Long userId);
}
