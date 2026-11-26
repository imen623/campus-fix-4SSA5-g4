package tn.esprit.campusfix.lostfound.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tn.esprit.campusfix.lostfound.client.dto.EventDTO;
import tn.esprit.campusfix.lostfound.client.dto.RoomDTO;
import tn.esprit.campusfix.lostfound.client.dto.UserDTO;

import java.time.LocalDate;

@Service
public class IntegrationClientService {

    private static final Logger log = LoggerFactory.getLogger(IntegrationClientService.class);

    @Value("${integration.mock-enabled:true}")
    private boolean mockEnabled;

    private final UserClient userClient;
    private final RoomClient roomClient;
    private final EventClient eventClient;

    public IntegrationClientService(UserClient userClient, RoomClient roomClient, EventClient eventClient) {
        this.userClient = userClient;
        this.roomClient = roomClient;
        this.eventClient = eventClient;
    }

    public UserDTO getUser(Long userId) {
        if (userId == null) {
            return null;
        }
        if (mockEnabled) {
            log.info("[Integration MOCK] Resolving mock user for ID: {}", userId);
            return UserDTO.builder()
                    .id(userId)
                    .nom("MockNom")
                    .prenom("MockPrenom")
                    .email("user" + userId + "@campusfix.tn")
                    .role("ETUDIANT")
                    .build();
        }
        try {
            return userClient.getUserById(userId);
        } catch (Exception e) {
            log.error("Error communicating with User microservice for userId {}: {}", userId, e.getMessage());
            return UserDTO.builder()
                    .id(userId)
                    .nom("FallbackUser")
                    .email("user" + userId + "@campusfix.tn")
                    .build();
        }
    }

    public RoomDTO getRoom(Long roomId) {
        if (roomId == null) {
            return null;
        }
        if (mockEnabled) {
            log.info("[Integration MOCK] Resolving mock room for ID: {}", roomId);
            return RoomDTO.builder()
                    .id(roomId)
                    .nom("Salle-A" + roomId)
                    .batiment("Bloc Info")
                    .capacite(25)
                    .disponible(true)
                    .build();
        }
        try {
            return roomClient.getRoomById(roomId);
        } catch (Exception e) {
            log.error("Error communicating with Room microservice for roomId {}: {}", roomId, e.getMessage());
            return RoomDTO.builder()
                    .id(roomId)
                    .nom("Salle-Secours-" + roomId)
                    .capacite(30)
                    .disponible(true)
                    .build();
        }
    }

    public EventDTO getEvent(Long eventId) {
        if (eventId == null) {
            return null;
        }
        if (mockEnabled) {
            log.info("[Integration MOCK] Resolving mock event for ID: {}", eventId);
            return EventDTO.builder()
                    .id(eventId)
                    .titre("Campus Event " + eventId)
                    .date(LocalDate.now())
                    .lieu("Amphi B")
                    .build();
        }
        try {
            return eventClient.getEventById(eventId);
        } catch (Exception e) {
            log.error("Error communicating with Event microservice for eventId {}: {}", eventId, e.getMessage());
            return EventDTO.builder()
                    .id(eventId)
                    .titre("Événement Indisponible " + eventId)
                    .date(LocalDate.now())
                    .lieu("Campus")
                    .build();
        }
    }

    public static String formatUserFullName(UserDTO user) {
        if (user == null) {
            return null;
        }
        if (user.getPrenom() == null || user.getPrenom().isBlank()) {
            return user.getNom();
        }
        return user.getNom() + " " + user.getPrenom();
    }

    public boolean isMockEnabled() {
        return mockEnabled;
    }
}
