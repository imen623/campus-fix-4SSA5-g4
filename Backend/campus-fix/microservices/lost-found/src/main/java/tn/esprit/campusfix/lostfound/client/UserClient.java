package tn.esprit.campusfix.lostfound.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.campusfix.lostfound.client.dto.UserDTO;

@FeignClient(name = "user-service", url = "${integration.user-service.url:http://localhost:8081}", fallback = UserClientFallback.class)
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}
