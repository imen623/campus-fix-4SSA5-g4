package tn.esprit.campusfix.lostfound.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tn.esprit.campusfix.lostfound.client.dto.EventDTO;

import java.time.LocalDate;

@Component
public class EventClientFallback implements EventClient {

    private static final Logger log = LoggerFactory.getLogger(EventClientFallback.class);

    @Override
    public EventDTO getEventById(Long id) {
        log.warn("Event microservice is unavailable. Fallback triggered for eventId: {}", id);
        return EventDTO.builder()
                .id(id)
                .titre("Événement Indisponible " + id)
                .date(LocalDate.now())
                .lieu("Campus")
                .build();
    }
}
