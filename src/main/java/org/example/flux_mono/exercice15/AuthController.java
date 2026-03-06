package org.example.flux_mono.exercice15;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Map<String, String> CREDENTIALS = Map.of(
            "alice", "alice123",
            "bob", "bob123",
            "admin", "admin123");

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        String expectedPassword = CREDENTIALS.get(request.getUsername());
        if (expectedPassword == null || !expectedPassword.equals(request.getPassword())) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "UNAUTHORIZED",
                            "message", "Invalid username or password")));
        }

        String token = jwtService.generateToken(request.getUsername());
        return Mono.just(ResponseEntity.ok(Map.of("token", token)));
    }
}
