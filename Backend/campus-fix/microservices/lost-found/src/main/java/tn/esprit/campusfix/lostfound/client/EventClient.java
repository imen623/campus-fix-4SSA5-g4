package tn.esprit.campusfix.lostfound.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.campusfix.lostfound.client.dto.EventDTO;

@FeignClient(name = "event-service", url = "${integration.event-service.url:http://localhost:8084}", fallback = EventClientFallback.class)
public interface EventClient {

    @GetMapping("/api/events/{id}")
    EventDTO getEventById(@PathVariable("id") Long id);
}
