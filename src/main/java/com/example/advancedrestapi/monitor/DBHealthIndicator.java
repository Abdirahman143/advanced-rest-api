package com.example.advancedrestapi.monitor;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class DBHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity("http://localhost:8080/actuator/health", Map.class);
        Map<String, Object> respMap = response.getBody();

        String status = (String) respMap.get("status");

        if (status.equals("UP")) {

            return Health.
                    up().
                    build();
        }

        return Health.down().build();

    }

}
