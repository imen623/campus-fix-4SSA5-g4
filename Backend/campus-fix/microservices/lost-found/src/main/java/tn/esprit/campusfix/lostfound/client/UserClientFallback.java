package tn.esprit.campusfix.lostfound.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tn.esprit.campusfix.lostfound.client.dto.UserDTO;

@Component
public class UserClientFallback implements UserClient {

    private static final Logger log = LoggerFactory.getLogger(UserClientFallback.class);

    @Override
    public UserDTO getUserById(Long id) {
        log.warn("User microservice is unavailable. Fallback triggered for userId: {}", id);
        return UserDTO.builder()
                .id(id)
                .nom("FallbackNom")
                .prenom("FallbackPrenom")
                .email("user" + id + "@campusfix.fallback")
                .role("ETUDIANT")
                .build();
    }
}
