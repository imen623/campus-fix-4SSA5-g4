package tn.esprit.campusfix.roomresource.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.campusfix.roomresource.enums.ReservationStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStatusDTO {

    @NotNull(message = "Le statut est obligatoire")
    private ReservationStatus statut;
}
