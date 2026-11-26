package tn.esprit.campusfix.lostfound.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.campusfix.lostfound.client.IntegrationClientService;
import tn.esprit.campusfix.lostfound.client.dto.UserDTO;
import tn.esprit.campusfix.lostfound.dto.request.ReclamationRequestDTO;
import tn.esprit.campusfix.lostfound.dto.request.ReclamationStatutUpdateDTO;
import tn.esprit.campusfix.lostfound.dto.response.ReclamationResponseDTO;
import tn.esprit.campusfix.lostfound.entity.*;
import tn.esprit.campusfix.lostfound.exception.BusinessRuleException;
import tn.esprit.campusfix.lostfound.exception.ResourceAlreadyExistsException;
import tn.esprit.campusfix.lostfound.exception.ResourceNotFoundException;
import tn.esprit.campusfix.lostfound.repository.DeclarationRepository;
import tn.esprit.campusfix.lostfound.repository.ReclamationRepository;
import tn.esprit.campusfix.lostfound.service.ReclamationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReclamationServiceImpl implements ReclamationService {

    private final ReclamationRepository reclamationRepository;
    private final DeclarationRepository declarationRepository;
    private final IntegrationClientService integrationClientService;

    public ReclamationServiceImpl(ReclamationRepository reclamationRepository,
                                   DeclarationRepository declarationRepository,
                                   IntegrationClientService integrationClientService) {
        this.reclamationRepository = reclamationRepository;
        this.declarationRepository = declarationRepository;
        this.integrationClientService = integrationClientService;
    }

    @Override
    public ReclamationResponseDTO createReclamation(ReclamationRequestDTO request) {
        Declaration declaration = declarationRepository.findById(request.getDeclarationId())
                .orElseThrow(() -> new ResourceNotFoundException("Déclaration non trouvée avec l'ID: " + request.getDeclarationId()));

        // Rule 1: Can only reclaim TROUVE declarations
        if (declaration.getTypeDeclaration() != TypeDeclaration.TROUVE) {
            throw new BusinessRuleException("On ne peut réclamer que des objets déclarés comme TROUVÉS.");
        }

        // Rule 2: Can't reclaim your own declaration
        if (declaration.getUserId().equals(request.getUserId())) {
            throw new BusinessRuleException("Vous ne pouvez pas réclamer votre propre déclaration.");
        }

        // Rule 3: Declaration must be EN_ATTENTE
        if (declaration.getStatut() != StatutDeclaration.EN_ATTENTE) {
            throw new BusinessRuleException("Impossible de créer une réclamation sur une déclaration au statut " + declaration.getStatut() + ".");
        }

        // Rule: No duplicate reclamation from same user on same declaration
        reclamationRepository.findByDeclarationIdAndUserId(request.getDeclarationId(), request.getUserId())
                .ifPresent(r -> { throw new ResourceAlreadyExistsException(
                        "Vous avez déjà une réclamation en cours pour cette déclaration (ID: " + r.getId() + ")."); });

        Reclamation reclamation = Reclamation.builder()
                .declaration(declaration)
                .userId(request.getUserId())
                .message(request.getMessage())
                .statut(StatutReclamation.EN_ATTENTE)
                .build();

        return toResponseDTO(reclamationRepository.save(reclamation));
    }

    @Override
    @Transactional(readOnly = true)
    public ReclamationResponseDTO getReclamationById(Long id) {
        Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réclamation non trouvée avec l'ID: " + id));
        return toResponseDTO(reclamation);
    }

    @Override
    public ReclamationResponseDTO updateStatut(Long id, ReclamationStatutUpdateDTO request) {
        Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réclamation non trouvée avec l'ID: " + id));

        if (reclamation.getStatut() == StatutReclamation.ACCEPTEE && request.getStatut() != StatutReclamation.ACCEPTEE) {
            throw new BusinessRuleException("Une réclamation déjà acceptée ne peut plus changer de statut.");
        }

        // Rule 4: When a reclamation is ACCEPTEE, the declaration becomes RESOLU
        // and other pending reclamations are rejected
        if (request.getStatut() == StatutReclamation.ACCEPTEE) {
            Declaration declaration = reclamation.getDeclaration();
            if (declaration.getStatut() != StatutDeclaration.EN_ATTENTE
                    || reclamationRepository.existsByDeclarationIdAndStatut(declaration.getId(), StatutReclamation.ACCEPTEE)) {
                throw new BusinessRuleException("Cette déclaration n'est plus ouverte aux réclamations ou possède déjà une réclamation acceptée.");
            }
            // Mark declaration as resolved
            declaration.setStatut(StatutDeclaration.RESOLU);
            declarationRepository.save(declaration);

            // Reject all other pending reclamations
            List<Reclamation> others = reclamationRepository.findByDeclarationIdAndStatut(
                    declaration.getId(), StatutReclamation.EN_ATTENTE);
            for (Reclamation other : others) {
                if (!other.getId().equals(id)) {
                    other.setStatut(StatutReclamation.REJETEE);
                    reclamationRepository.save(other);
                }
            }
        }

        reclamation.setStatut(request.getStatut());
        return toResponseDTO(reclamationRepository.save(reclamation));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReclamationResponseDTO> getReclamationsByDeclaration(Long declarationId) {
        if (!declarationRepository.existsById(declarationId)) {
            throw new ResourceNotFoundException("Déclaration non trouvée avec l'ID: " + declarationId);
        }
        return reclamationRepository.findByDeclarationId(declarationId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReclamationResponseDTO> getReclamationsByUser(Long userId) {
        return reclamationRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private ReclamationResponseDTO toResponseDTO(Reclamation r) {
        UserDTO user = integrationClientService.getUser(r.getUserId());
        return ReclamationResponseDTO.builder()
                .id(r.getId())
                .declarationId(r.getDeclaration().getId())
                .declarationTitre(r.getDeclaration().getTitre())
                .userId(r.getUserId())
                .reclamantNom(IntegrationClientService.formatUserFullName(user))
                .reclamantEmail(user != null ? user.getEmail() : null)
                .message(r.getMessage())
                .statut(r.getStatut())
                .dateReclamation(r.getDateReclamation())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
