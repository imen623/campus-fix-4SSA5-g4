package tn.esprit.campusfix.lostfound.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.esprit.campusfix.lostfound.dto.request.DeclarationRequestDTO;
import tn.esprit.campusfix.lostfound.dto.request.DeclarationStatutUpdateDTO;
import tn.esprit.campusfix.lostfound.dto.response.DeclarationResponseDTO;
import tn.esprit.campusfix.lostfound.dto.response.DeclarationStatsDTO;
import tn.esprit.campusfix.lostfound.entity.CategorieObjet;
import tn.esprit.campusfix.lostfound.entity.StatutDeclaration;
import tn.esprit.campusfix.lostfound.entity.TypeDeclaration;

import java.util.List;

public interface DeclarationService {

    DeclarationResponseDTO createDeclaration(DeclarationRequestDTO request);

    Page<DeclarationResponseDTO> getAllDeclarations(TypeDeclaration type, CategorieObjet categorie, StatutDeclaration statut, Pageable pageable);

    DeclarationResponseDTO getDeclarationById(Long id);

    DeclarationResponseDTO updateDeclaration(Long id, DeclarationRequestDTO request);

    void deleteDeclaration(Long id);

    DeclarationResponseDTO updateStatut(Long id, DeclarationStatutUpdateDTO request);

    List<DeclarationResponseDTO> getDeclarationsByUser(Long userId);

    DeclarationStatsDTO getStats(Long declarationId);
}
