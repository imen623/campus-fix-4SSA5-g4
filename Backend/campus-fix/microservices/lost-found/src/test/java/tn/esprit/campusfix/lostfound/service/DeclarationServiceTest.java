package tn.esprit.campusfix.lostfound.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import tn.esprit.campusfix.lostfound.client.IntegrationClientService;
import tn.esprit.campusfix.lostfound.client.dto.EventDTO;
import tn.esprit.campusfix.lostfound.client.dto.RoomDTO;
import tn.esprit.campusfix.lostfound.client.dto.UserDTO;
import tn.esprit.campusfix.lostfound.dto.request.DeclarationRequestDTO;
import tn.esprit.campusfix.lostfound.dto.response.DeclarationResponseDTO;
import tn.esprit.campusfix.lostfound.dto.response.DeclarationStatsDTO;
import tn.esprit.campusfix.lostfound.entity.*;
import tn.esprit.campusfix.lostfound.exception.ResourceNotFoundException;
import tn.esprit.campusfix.lostfound.repository.DeclarationRepository;
import tn.esprit.campusfix.lostfound.repository.ReclamationRepository;
import tn.esprit.campusfix.lostfound.service.impl.DeclarationServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeclarationServiceTest {

    @Mock
    private DeclarationRepository declarationRepository;

    @Mock
    private ReclamationRepository reclamationRepository;

    @Mock
    private IntegrationClientService integrationClientService;

    @InjectMocks
    private DeclarationServiceImpl declarationService;

    @Test
    @DisplayName("Création d'une déclaration enrichie avec User, Room et Event")
    void testCreateDeclaration_EnrichesUserRoomAndEvent() {
        DeclarationRequestDTO request = DeclarationRequestDTO.builder()
                .titre("Sac à dos bleu")
                .description("Oublié près de l'amphi")
                .categorie(CategorieObjet.SAC)
                .typeDeclaration(TypeDeclaration.TROUVE)
                .userId(1L)
                .salleId(3L)
                .eventId(10L)
                .lieu("Bloc A")
                .build();

        when(declarationRepository.save(any(Declaration.class))).thenAnswer(inv -> {
            Declaration d = inv.getArgument(0);
            d.setId(5L);
            return d;
        });
        when(integrationClientService.getUser(1L))
                .thenReturn(UserDTO.builder().id(1L).nom("Ben Ali").prenom("Sami").email("sami@campusfix.tn").build());
        when(integrationClientService.getRoom(3L))
                .thenReturn(RoomDTO.builder().id(3L).nom("Salle-A3").build());
        when(integrationClientService.getEvent(10L))
                .thenReturn(EventDTO.builder().id(10L).titre("Journée Portes Ouvertes").build());

        DeclarationResponseDTO response = declarationService.createDeclaration(request);

        assertEquals(5L, response.getId());
        assertEquals(StatutDeclaration.EN_ATTENTE, response.getStatut());
        assertEquals("Ben Ali Sami", response.getDeclarantNom());
        assertEquals("Salle-A3", response.getSalleNom());
        assertEquals("Journée Portes Ouvertes", response.getEventTitre());
    }

    @Test
    @DisplayName("Filtrage par Specification retourne une page de déclarations mappées")
    @SuppressWarnings("unchecked")
    void testGetAllDeclarations_UsesSpecification() {
        Declaration declaration = Declaration.builder()
                .id(1L)
                .titre("Clés")
                .categorie(CategorieObjet.CLE)
                .typeDeclaration(TypeDeclaration.TROUVE)
                .statut(StatutDeclaration.EN_ATTENTE)
                .userId(2L)
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        when(declarationRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(declaration)));
        when(integrationClientService.getUser(2L))
                .thenReturn(UserDTO.builder().id(2L).nom("FallbackUser").email("u2@campusfix.tn").build());

        Page<DeclarationResponseDTO> result = declarationService.getAllDeclarations(
                null, CategorieObjet.CLE, StatutDeclaration.EN_ATTENTE, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("FallbackUser", result.getContent().get(0).getDeclarantNom());
        verify(declarationRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Calcul des statistiques de réclamations sur une déclaration")
    void testGetStats_ReturnsCounts() {
        Declaration declaration = Declaration.builder()
                .id(3L)
                .titre("Clés VW")
                .statut(StatutDeclaration.RESOLU)
                .build();

        List<Reclamation> recs = List.of(
                Reclamation.builder().id(1L).statut(StatutReclamation.ACCEPTEE).build(),
                Reclamation.builder().id(2L).statut(StatutReclamation.REJETEE).build(),
                Reclamation.builder().id(3L).statut(StatutReclamation.EN_ATTENTE).build()
        );

        when(declarationRepository.findById(3L)).thenReturn(Optional.of(declaration));
        when(reclamationRepository.findByDeclarationId(3L)).thenReturn(recs);

        DeclarationStatsDTO stats = declarationService.getStats(3L);

        assertEquals(3, stats.getTotalReclamations());
        assertEquals(1, stats.getReclamationsAcceptees());
        assertEquals(1, stats.getReclamationsRejetees());
        assertEquals(1, stats.getReclamationsEnAttente());
        assertTrue(stats.isEstResolu());
    }

    @Test
    @DisplayName("Consultation d'une déclaration inexistante → ResourceNotFoundException")
    void testGetDeclarationById_NotFound() {
        when(declarationRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> declarationService.getDeclarationById(999L));
    }
}
