package tn.esprit.campusfix.roomresource.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.campusfix.roomresource.dto.ReservationRequestDTO;
import tn.esprit.campusfix.roomresource.dto.ReservationResponseDTO;
import tn.esprit.campusfix.roomresource.dto.RessourceResponseDTO;
import tn.esprit.campusfix.roomresource.entity.Reservation;
import tn.esprit.campusfix.roomresource.entity.Ressource;
import tn.esprit.campusfix.roomresource.enums.ReservationStatus;
import tn.esprit.campusfix.roomresource.enums.RessourceStatus;
import tn.esprit.campusfix.roomresource.exception.BusinessConflictException;
import tn.esprit.campusfix.roomresource.exception.InvalidPeriodException;
import tn.esprit.campusfix.roomresource.exception.ResourceNotFoundException;
import tn.esprit.campusfix.roomresource.repository.ReservationRepository;
import tn.esprit.campusfix.roomresource.repository.RessourceRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RessourceRepository ressourceRepository;
    private final RessourceServiceImpl ressourceService;

    @Override
    public ReservationResponseDTO createReservation(ReservationRequestDTO dto) {
        // Regle 2 : La date de debut doit etre strictement anterieure a la date de fin
        if (!dto.getDateDebut().isBefore(dto.getDateFin())) {
            throw new InvalidPeriodException("La date de debut doit etre strictement anterieure a la date de fin");
        }

        // Regle 1 : Une reservation doit concerner au moins une ressource
        if (dto.getRessourceIds() == null || dto.getRessourceIds().isEmpty()) {
            throw new BusinessConflictException("Une reservation doit concerner au moins une ressource");
        }

        Set<Ressource> ressourcesToReserve = new HashSet<>();

        for (Long ressourceId : dto.getRessourceIds()) {
            Ressource ressource = ressourceRepository.findById(ressourceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Ressource introuvable avec l'identifiant : " + ressourceId));

            // Regle 3 & 4 : La ressource doit etre ACTIVE (pas en maintenance ni inactive)
            if (ressource.getStatut() != RessourceStatus.ACTIVE) {
                throw new BusinessConflictException("La ressource '" + ressource.getNom() + "' a le statut " + ressource.getStatut() + " et ne peut pas etre reservee");
            }

            // Regle 5 : Verification des chevauchements avec des reservations CONFIRMEES existantes
            List<Reservation> conflicts = reservationRepository.findOverlappingConfirmedReservations(
                    ressourceId,
                    dto.getDateDebut(),
                    dto.getDateFin()
            );

            if (!conflicts.isEmpty()) {
                throw new BusinessConflictException("Conflit detecte : La ressource '" + ressource.getNom() + "' fait deja l'objet d'une reservation confirmee sur ce creneau");
            }

            ressourcesToReserve.add(ressource);
        }

        // Creation de l'entite Reservation (statut par defaut : EN_ATTENTE)
        Reservation reservation = Reservation.builder()
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .motif(dto.getMotif())
                .statut(ReservationStatus.EN_ATTENTE)
                .userId(dto.getUserId())
                .eventId(dto.getEventId())
                .build();

        // Liaison Many-to-Many
        for (Ressource ressource : ressourcesToReserve) {
            reservation.addRessource(ressource);
        }

        Reservation saved = reservationRepository.save(reservation);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO getReservationById(Long id) {
        return mapToResponse(findEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservationsByEventId(Long eventId) {
        return reservationRepository.findByEventId(eventId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservationsByStatut(ReservationStatus statut) {
        return reservationRepository.findByStatut(statut)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ReservationResponseDTO updateReservationStatus(Long id, ReservationStatus newStatut) {
        Reservation reservation = findEntityById(id);

        // Si on confirme la reservation, on re-verifie qu'aucun conflit n'est apparu entre-temps
        if (newStatut == ReservationStatus.CONFIRMEE) {
            for (Ressource ressource : reservation.getRessources()) {
                if (ressource.getStatut() != RessourceStatus.ACTIVE) {
                    throw new BusinessConflictException("Impossible de confirmer : la ressource '" + ressource.getNom() + "' n'est plus active");
                }

                List<Reservation> conflicts = reservationRepository.findOverlappingConfirmedReservationsExcludingId(
                        ressource.getId(),
                        reservation.getId(),
                        reservation.getDateDebut(),
                        reservation.getDateFin()
                );

                if (!conflicts.isEmpty()) {
                    throw new BusinessConflictException("Impossible de confirmer : conflit de creneau detecte pour la ressource '" + ressource.getNom() + "'");
                }
            }
        }

        reservation.setStatut(newStatut);
        Reservation updated = reservationRepository.save(reservation);
        return mapToResponse(updated);
    }

    @Override
    public void cancelReservation(Long id) {
        Reservation reservation = findEntityById(id);
        reservation.setStatut(ReservationStatus.ANNULEE);
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkAvailability(Long ressourceId, LocalDateTime debut, LocalDateTime fin) {
        if (!debut.isBefore(fin)) {
            throw new InvalidPeriodException("La date de debut doit etre anterieure a la date de fin");
        }

        Ressource ressource = ressourceRepository.findById(ressourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Ressource introuvable : " + ressourceId));

        if (ressource.getStatut() != RessourceStatus.ACTIVE) {
            return false;
        }

        List<Reservation> conflicts = reservationRepository.findOverlappingConfirmedReservations(ressourceId, debut, fin);
        return conflicts.isEmpty();
    }

    private Reservation findEntityById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation introuvable avec l'identifiant : " + id));
    }

    private ReservationResponseDTO mapToResponse(Reservation entity) {
        Set<RessourceResponseDTO> ressourceDTOs = entity.getRessources()
                .stream()
                .map(ressourceService::mapToResponse)
                .collect(Collectors.toSet());

        return ReservationResponseDTO.builder()
                .id(entity.getId())
                .dateDebut(entity.getDateDebut())
                .dateFin(entity.getDateFin())
                .motif(entity.getMotif())
                .statut(entity.getStatut())
                .dateCreation(entity.getDateCreation())
                .userId(entity.getUserId())
                .eventId(entity.getEventId())
                .ressources(ressourceDTOs)
                .build();
    }
}
