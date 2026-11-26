package tn.esprit.campusfix.lostfound.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.campusfix.lostfound.client.dto.RoomDTO;

@FeignClient(name = "room-service", url = "${integration.room-service.url:http://localhost:8082}", fallback = RoomClientFallback.class)
public interface RoomClient {

    @GetMapping("/api/rooms/{id}")
    RoomDTO getRoomById(@PathVariable("id") Long id);
}
