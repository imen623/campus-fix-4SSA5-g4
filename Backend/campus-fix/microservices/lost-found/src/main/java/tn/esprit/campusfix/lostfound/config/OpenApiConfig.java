package tn.esprit.campusfix.lostfound.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8085}")
    private String serverPort;

    @Bean
    public OpenAPI lostFoundOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Lost & Found Microservice API")
                        .description("API REST pour la gestion des objets perdus et trouvés sur le campus CampusFix. "
                                + "Inclut la gestion des déclarations (perdus/trouvés) et des réclamations.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("CampusFix Team")
                                .email("contact@campusfix.tn")))
                .servers(List.of(
                        new Server().url("http://localhost:" + serverPort).description("Serveur Local Development")
                ));
    }
}
