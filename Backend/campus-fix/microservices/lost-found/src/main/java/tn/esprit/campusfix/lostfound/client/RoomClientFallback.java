package tn.esprit.campusfix.lostfound.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tn.esprit.campusfix.lostfound.client.dto.RoomDTO;

@Component
public class RoomClientFallback implements RoomClient {

    private static final Logger log = LoggerFactory.getLogger(RoomClientFallback.class);

    @Override
    public RoomDTO getRoomById(Long id) {
        log.warn("Room microservice is unavailable. Fallback triggered for roomId: {}", id);
        return RoomDTO.builder()
                .id(id)
                .nom("Salle-Secours-" + id)
                .capacite(30)
                .disponible(true)
                .build();
    }
}
