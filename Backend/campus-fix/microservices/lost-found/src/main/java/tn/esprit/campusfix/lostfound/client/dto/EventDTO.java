package tn.esprit.campusfix.lostfound.client.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {
    private Long id;
    private String titre;
    private LocalDate date;
    private String lieu;
}
