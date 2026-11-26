package tn.esprit.campusfix.lostfound.service.impl;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.campusfix.lostfound.client.IntegrationClientService;
import tn.esprit.campusfix.lostfound.client.dto.EventDTO;
import tn.esprit.campusfix.lostfound.client.dto.RoomDTO;
import tn.esprit.campusfix.lostfound.client.dto.UserDTO;
import tn.esprit.campusfix.lostfound.dto.request.DeclarationRequestDTO;
import tn.esprit.campusfix.lostfound.dto.request.DeclarationStatutUpdateDTO;
import tn.esprit.campusfix.lostfound.dto.response.DeclarationResponseDTO;
import tn.esprit.campusfix.lostfound.dto.response.DeclarationStatsDTO;
import tn.esprit.campusfix.lostfound.entity.*;
import tn.esprit.campusfix.lostfound.exception.ResourceNotFoundException;
import tn.esprit.campusfix.lostfound.repository.DeclarationRepository;
import tn.esprit.campusfix.lostfound.repository.ReclamationRepository;
import tn.esprit.campusfix.lostfound.service.DeclarationService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeclarationServiceImpl implements DeclarationService {

    private final DeclarationRepository declarationRepository;
    private final ReclamationRepository reclamationRepository;
    private final IntegrationClientService integrationClientService;

    public DeclarationServiceImpl(DeclarationRepository declarationRepository,
                                  ReclamationRepository reclamationRepository,
                                  IntegrationClientService integrationClientService) {
        this.declarationRepository = declarationRepository;
        this.reclamationRepository = reclamationRepository;
        this.integrationClientService = integrationClientService;
    }

    @Override
    public DeclarationResponseDTO createDeclaration(DeclarationRequestDTO request) {
        Declaration declaration = Declaration.builder()
                .titre(request.getTitre())
                .description(request.getDescription())
                .categorie(request.getCategorie())
                .typeDeclaration(request.getTypeDeclaration())
                .statut(StatutDeclaration.EN_ATTENTE)
                .userId(request.getUserId())
                .salleId(request.getSalleId())
                .eventId(request.getEventId())
                .lieu(request.getLieu())
                .dateEvenement(request.getDateEvenement())
                .imageUrl(request.getImageUrl())
                .build();
        declaration = declarationRepository.save(declaration);
        return toResponseDTO(declaration);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeclarationResponseDTO> getAllDeclarations(TypeDeclaration type, CategorieObjet categorie, StatutDeclaration statut, Pageable pageable) {
        Specification<Declaration> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (type != null) {
                predicates.add(cb.equal(root.get("typeDeclaration"), type));
            }
            if (categorie != null) {
                predicates.add(cb.equal(root.get("categorie"), categorie));
            }
            if (statut != null) {
                predicates.add(cb.equal(root.get("statut"), statut));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return declarationRepository.findAll(spec, pageable).map(this::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public DeclarationResponseDTO getDeclarationById(Long id) {
        Declaration declaration = declarationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Déclaration non trouvée avec l'ID: " + id));
        return toResponseDTO(declaration);
    }

    @Override
    public DeclarationResponseDTO updateDeclaration(Long id, DeclarationRequestDTO request) {
        Declaration declaration = declarationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Déclaration non trouvée avec l'ID: " + id));
        declaration.setTitre(request.getTitre());
        declaration.setDescription(request.getDescription());
        declaration.setCategorie(request.getCategorie());
        declaration.setTypeDeclaration(request.getTypeDeclaration());
        declaration.setSalleId(request.getSalleId());
        declaration.setEventId(request.getEventId());
        declaration.setLieu(request.getLieu());
        declaration.setDateEvenement(request.getDateEvenement());
        declaration.setImageUrl(request.getImageUrl());
        return toResponseDTO(declarationRepository.save(declaration));
    }

    @Override
    public void deleteDeclaration(Long id) {
        Declaration declaration = declarationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Déclaration non trouvée avec l'ID: " + id));
        declarationRepository.delete(declaration);
    }

    @Override
    public DeclarationResponseDTO updateStatut(Long id, DeclarationStatutUpdateDTO request) {
        Declaration declaration = declarationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Déclaration non trouvée avec l'ID: " + id));
        declaration.setStatut(request.getStatut());
        return toResponseDTO(declarationRepository.save(declaration));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeclarationResponseDTO> getDeclarationsByUser(Long userId) {
        return declarationRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DeclarationStatsDTO getStats(Long declarationId) {
        Declaration declaration = declarationRepository.findById(declarationId)
                .orElseThrow(() -> new ResourceNotFoundException("Déclaration non trouvée avec l'ID: " + declarationId));
        List<Reclamation> reclamations = reclamationRepository.findByDeclarationId(declarationId);
        long enAttente = reclamations.stream().filter(r -> r.getStatut() == StatutReclamation.EN_ATTENTE).count();
        long acceptees = reclamations.stream().filter(r -> r.getStatut() == StatutReclamation.ACCEPTEE).count();
        long rejetees = reclamations.stream().filter(r -> r.getStatut() == StatutReclamation.REJETEE).count();
        return DeclarationStatsDTO.builder()
                .declarationId(declarationId)
                .titre(declaration.getTitre())
                .totalReclamations(reclamations.size())
                .reclamationsEnAttente((int) enAttente)
                .reclamationsAcceptees((int) acceptees)
                .reclamationsRejetees((int) rejetees)
                .estResolu(declaration.getStatut() == StatutDeclaration.RESOLU)
                .build();
    }

    private DeclarationResponseDTO toResponseDTO(Declaration d) {
        UserDTO user = integrationClientService.getUser(d.getUserId());
        RoomDTO room = d.getSalleId() != null ? integrationClientService.getRoom(d.getSalleId()) : null;
        EventDTO event = d.getEventId() != null ? integrationClientService.getEvent(d.getEventId()) : null;

        return DeclarationResponseDTO.builder()
                .id(d.getId())
                .titre(d.getTitre())
                .description(d.getDescription())
                .categorie(d.getCategorie())
                .typeDeclaration(d.getTypeDeclaration())
                .statut(d.getStatut())
                .userId(d.getUserId())
                .declarantNom(IntegrationClientService.formatUserFullName(user))
                .declarantEmail(user != null ? user.getEmail() : null)
                .salleId(d.getSalleId())
                .salleNom(room != null ? room.getNom() : null)
                .eventId(d.getEventId())
                .eventTitre(event != null ? event.getTitre() : null)
                .lieu(d.getLieu())
                .dateEvenement(d.getDateEvenement())
                .imageUrl(d.getImageUrl())
                .nbReclamations(d.getReclamations() != null ? d.getReclamations().size() : 0)
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }
}
