package tn.esprit.campusfix.lostfound.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.campusfix.lostfound.client.IntegrationClientService;
import tn.esprit.campusfix.lostfound.client.dto.UserDTO;
import tn.esprit.campusfix.lostfound.dto.request.ReclamationRequestDTO;
import tn.esprit.campusfix.lostfound.dto.request.ReclamationStatutUpdateDTO;
import tn.esprit.campusfix.lostfound.dto.response.ReclamationResponseDTO;
import tn.esprit.campusfix.lostfound.entity.*;
import tn.esprit.campusfix.lostfound.exception.BusinessRuleException;
import tn.esprit.campusfix.lostfound.exception.ResourceAlreadyExistsException;
import tn.esprit.campusfix.lostfound.repository.DeclarationRepository;
import tn.esprit.campusfix.lostfound.repository.ReclamationRepository;
import tn.esprit.campusfix.lostfound.service.impl.ReclamationServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReclamationServiceTest {

    @Mock
    private ReclamationRepository reclamationRepository;

    @Mock
    private DeclarationRepository declarationRepository;

    @Mock
    private IntegrationClientService integrationClientService;

    @InjectMocks
    private ReclamationServiceImpl reclamationService;

    private Declaration foundDeclaration;
    private Declaration lostDeclaration;
    private Declaration resolvedDeclaration;

    @BeforeEach
    void setUp() {
        foundDeclaration = Declaration.builder()
                .id(10L)
                .titre("Clés trouvées")
                .typeDeclaration(TypeDeclaration.TROUVE)
                .statut(StatutDeclaration.EN_ATTENTE)
                .userId(1L)
                .build();

        lostDeclaration = Declaration.builder()
                .id(20L)
                .titre("MacBook perdu")
                .typeDeclaration(TypeDeclaration.PERDU)
                .statut(StatutDeclaration.EN_ATTENTE)
                .userId(2L)
                .build();

        resolvedDeclaration = Declaration.builder()
                .id(30L)
                .titre("Objet résolu")
                .typeDeclaration(TypeDeclaration.TROUVE)
                .statut(StatutDeclaration.RESOLU)
                .userId(3L)
                .build();
    }

    @Test
    @DisplayName("Création d'une réclamation valide sur un objet TROUVÉ → statut EN_ATTENTE")
    void testCreateReclamation_Success() {
        ReclamationRequestDTO request = ReclamationRequestDTO.builder()
                .declarationId(10L)
                .userId(99L)
                .message("Je pense que ces clés sont les miennes.")
                .build();

        when(declarationRepository.findById(10L)).thenReturn(Optional.of(foundDeclaration));
        when(reclamationRepository.findByDeclarationIdAndUserId(10L, 99L)).thenReturn(Optional.empty());
        when(integrationClientService.getUser(99L)).thenReturn(UserDTO.builder().id(99L).nom("Mock").prenom("User").email("mock@test.tn").build());
        when(reclamationRepository.save(any(Reclamation.class))).thenAnswer(inv -> {
            Reclamation r = inv.getArgument(0);
            r.setId(1L);
            return r;
        });

        ReclamationResponseDTO response = reclamationService.createReclamation(request);

        assertNotNull(response);
        assertEquals(StatutReclamation.EN_ATTENTE, response.getStatut());
        verify(reclamationRepository, times(1)).save(any(Reclamation.class));
    }

    @Test
    @DisplayName("Réclamation sur un objet PERDU → BusinessRuleException")
    void testCreateReclamation_OnLostDeclaration_ThrowsBusinessRule() {
        ReclamationRequestDTO request = ReclamationRequestDTO.builder()
                .declarationId(20L)
                .userId(99L)
                .build();

        when(declarationRepository.findById(20L)).thenReturn(Optional.of(lostDeclaration));

        assertThrows(BusinessRuleException.class, () -> reclamationService.createReclamation(request));
        verify(reclamationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Réclamation par le propriétaire de la déclaration → BusinessRuleException")
    void testCreateReclamation_ByOwner_ThrowsBusinessRule() {
        ReclamationRequestDTO request = ReclamationRequestDTO.builder()
                .declarationId(10L)
                .userId(1L) // same as foundDeclaration.userId
                .build();

        when(declarationRepository.findById(10L)).thenReturn(Optional.of(foundDeclaration));

        assertThrows(BusinessRuleException.class, () -> reclamationService.createReclamation(request));
        verify(reclamationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Double réclamation du même utilisateur → ResourceAlreadyExistsException (409)")
    void testCreateReclamation_Duplicate_ThrowsConflict() {
        ReclamationRequestDTO request = ReclamationRequestDTO.builder()
                .declarationId(10L)
                .userId(99L)
                .build();

        Reclamation existing = Reclamation.builder()
                .id(5L)
                .declaration(foundDeclaration)
                .userId(99L)
                .statut(StatutReclamation.EN_ATTENTE)
                .build();

        when(declarationRepository.findById(10L)).thenReturn(Optional.of(foundDeclaration));
        when(reclamationRepository.findByDeclarationIdAndUserId(10L, 99L)).thenReturn(Optional.of(existing));

        assertThrows(ResourceAlreadyExistsException.class, () -> reclamationService.createReclamation(request));
        verify(reclamationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Réclamation sur déclaration RÉSOLUE → BusinessRuleException")
    void testCreateReclamation_OnResolvedDeclaration_ThrowsBusinessRule() {
        ReclamationRequestDTO request = ReclamationRequestDTO.builder()
                .declarationId(30L)
                .userId(99L)
                .build();

        when(declarationRepository.findById(30L)).thenReturn(Optional.of(resolvedDeclaration));

        assertThrows(BusinessRuleException.class, () -> reclamationService.createReclamation(request));
        verify(reclamationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Accepter une réclamation → la déclaration devient RÉSOLUE, les autres réclamations sont rejetées")
    void testUpdateStatut_Accept_ResolvesDeclarationAndRejectsOthers() {
        Reclamation target = Reclamation.builder()
                .id(1L)
                .declaration(foundDeclaration)
                .userId(99L)
                .statut(StatutReclamation.EN_ATTENTE)
                .build();

        Reclamation other = Reclamation.builder()
                .id(2L)
                .declaration(foundDeclaration)
                .userId(88L)
                .statut(StatutReclamation.EN_ATTENTE)
                .build();

        ReclamationStatutUpdateDTO updateDTO = ReclamationStatutUpdateDTO.builder()
                .statut(StatutReclamation.ACCEPTEE)
                .build();

        when(reclamationRepository.findById(1L)).thenReturn(Optional.of(target));
        when(reclamationRepository.existsByDeclarationIdAndStatut(10L, StatutReclamation.ACCEPTEE)).thenReturn(false);
        when(reclamationRepository.findByDeclarationIdAndStatut(10L, StatutReclamation.EN_ATTENTE))
                .thenReturn(List.of(target, other));
        when(reclamationRepository.save(any(Reclamation.class))).thenAnswer(inv -> inv.getArgument(0));
        when(declarationRepository.save(any(Declaration.class))).thenReturn(foundDeclaration);
        when(integrationClientService.getUser(99L)).thenReturn(UserDTO.builder().id(99L).nom("Mock").email("m@m.tn").build());

        ReclamationResponseDTO response = reclamationService.updateStatut(1L, updateDTO);

        assertEquals(StatutDeclaration.RESOLU, foundDeclaration.getStatut());
        assertEquals(StatutReclamation.REJETEE, other.getStatut());
        assertEquals(StatutReclamation.ACCEPTEE, target.getStatut());
        verify(declarationRepository, times(1)).save(foundDeclaration);
    }

    @Test
    @DisplayName("Réclamation sur déclaration ARCHIVÉE → BusinessRuleException")
    void testCreateReclamation_OnArchivedDeclaration_ThrowsBusinessRule() {
        Declaration archivedDeclaration = Declaration.builder()
                .id(40L)
                .titre("Objet archivé")
                .typeDeclaration(TypeDeclaration.TROUVE)
                .statut(StatutDeclaration.ARCHIVE)
                .userId(3L)
                .build();

        ReclamationRequestDTO request = ReclamationRequestDTO.builder()
                .declarationId(40L)
                .userId(99L)
                .build();

        when(declarationRepository.findById(40L)).thenReturn(Optional.of(archivedDeclaration));

        assertThrows(BusinessRuleException.class, () -> reclamationService.createReclamation(request));
        verify(reclamationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Modifier le statut d'une réclamation déjà ACCEPTEE → BusinessRuleException")
    void testUpdateStatut_AlreadyAccepted_ThrowsBusinessRule() {
        Reclamation accepted = Reclamation.builder()
                .id(1L)
                .declaration(resolvedDeclaration)
                .userId(99L)
                .statut(StatutReclamation.ACCEPTEE)
                .build();

        ReclamationStatutUpdateDTO updateDTO = ReclamationStatutUpdateDTO.builder()
                .statut(StatutReclamation.REJETEE)
                .build();

        when(reclamationRepository.findById(1L)).thenReturn(Optional.of(accepted));

        assertThrows(BusinessRuleException.class, () -> reclamationService.updateStatut(1L, updateDTO));
        verify(reclamationRepository, never()).save(any());
    }
}
