package tn.esprit.campusfix.lostfound.client.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {
    private Long id;
    private String nom;
    private String batiment;
    private Integer capacite;
    private Boolean disponible;
}
