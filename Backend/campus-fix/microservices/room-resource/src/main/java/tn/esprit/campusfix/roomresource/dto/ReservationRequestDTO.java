package tn.esprit.campusfix.roomresource.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDTO {

    @NotNull(message = "La date de debut est obligatoire")
    @Future(message = "La date de debut doit etre dans le futur")
    private LocalDateTime dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @Future(message = "La date de fin doit etre dans le futur")
    private LocalDateTime dateFin;

    @NotBlank(message = "Le motif de la reservation est obligatoire")
    private String motif;

    @NotNull(message = "L'identifiant de l'utilisateur (userId) est obligatoire")
    private Long userId;

    private Long eventId;

    @NotEmpty(message = "La reservation doit concerner au moins une ressource")
    private Set<Long> ressourceIds;
}
