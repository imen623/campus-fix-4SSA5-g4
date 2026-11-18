package tn.esprit.campusfix.roomresource.dto;

import lombok.*;
import tn.esprit.campusfix.roomresource.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDTO {

    private Long id;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String motif;
    private ReservationStatus statut;
    private LocalDateTime dateCreation;
    private Long userId;
    private Long eventId;
    private Set<RessourceResponseDTO> ressources;
}
