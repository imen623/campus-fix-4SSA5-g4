package tn.esprit.campusfix.roomresource.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CampusFix - Room & Resource Microservice API")
                        .version("1.0.0")
                        .description("API REST de gestion des salles, equipements universitaires et moteur de reservation avec prevention des conflits temporels.")
                        .contact(new Contact()
                                .name("CampusFix Team")
                                .email("contact@campusfix.esprit.tn"))
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")))
                .servers(List.of(
                        new Server().url("http://localhost:8086").description("Serveur Local de Developpement"),
                        new Server().url("http://localhost:8080/api/room-resource").description("Via Spring Cloud API Gateway")
                ));
    }
}
