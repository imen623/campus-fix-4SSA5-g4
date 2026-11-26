package tn.esprit.campusfix.lostfound.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class IntegrationHealthIndicator implements HealthIndicator {

    private final boolean mockMode;

    public IntegrationHealthIndicator(@Value("${integration.mock-enabled:true}") boolean mockMode) {
        this.mockMode = mockMode;
    }

    @Override
    public Health health() {
        return Health.up()
                .withDetail("mockMode", mockMode)
                .withDetail("service", "lost-found")
                .build();
    }
}
