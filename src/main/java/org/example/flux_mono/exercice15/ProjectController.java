package org.example.flux_mono.exercice15;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @GetMapping
    public Mono<ResponseEntity<Map<String, List<String>>>> getProjects(ServerWebExchange exchange) {
        String username = exchange.getAttribute("authenticatedUsername");
        if (username == null || username.isBlank()) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        List<String> projects = switch (username.toLowerCase()) {
            case "alice" -> List.of("Projet A", "Projet B");
            case "bob" -> List.of("Projet X", "Projet Y");
            case "admin" -> List.of("Projet Admin", "Projet Audit");
            default -> List.of("Projet Personnel");
        };

        return Mono.just(ResponseEntity.ok(Map.of("projects", projects)));
    }
}
