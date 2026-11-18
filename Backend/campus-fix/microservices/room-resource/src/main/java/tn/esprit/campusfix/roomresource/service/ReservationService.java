package tn.esprit.campusfix.roomresource.service;

import tn.esprit.campusfix.roomresource.dto.ReservationRequestDTO;
import tn.esprit.campusfix.roomresource.dto.ReservationResponseDTO;
import tn.esprit.campusfix.roomresource.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    ReservationResponseDTO createReservation(ReservationRequestDTO dto);

    ReservationResponseDTO getReservationById(Long id);

    List<ReservationResponseDTO> getAllReservations();

    List<ReservationResponseDTO> getReservationsByUserId(Long userId);

    List<ReservationResponseDTO> getReservationsByEventId(Long eventId);

    List<ReservationResponseDTO> getReservationsByStatut(ReservationStatus statut);

    ReservationResponseDTO updateReservationStatus(Long id, ReservationStatus newStatut);

    void cancelReservation(Long id);

    boolean checkAvailability(Long ressourceId, LocalDateTime debut, LocalDateTime fin);
}
