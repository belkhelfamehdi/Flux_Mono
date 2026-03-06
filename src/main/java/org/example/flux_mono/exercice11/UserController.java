package org.example.flux_mono.exercice11;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Flux<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<User>> createUser(@RequestBody User user) {
        return Mono.defer(() -> userService.create(user))
            .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
            .onErrorResume(IllegalArgumentException.class,
                ex -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
        return Mono.defer(() -> userService.update(id, user))
                .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorResume(IllegalArgumentException.class,
                ex -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return userService.findById(id)
                .flatMap(existing -> userService.delete(id)
                        .thenReturn(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
