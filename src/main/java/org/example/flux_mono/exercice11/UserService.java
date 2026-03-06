package org.example.flux_mono.exercice11;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<User> create(User user) {
        validate(user);
        user.setId(null);
        return userRepository.save(user);
    }

    public Mono<User> update(Long id, User user) {
        validate(user);
        return userRepository.findById(id)
                .flatMap(existing -> {
                    existing.setName(user.getName());
                    existing.setEmail(user.getEmail());
                    existing.setActive(user.isActive());
                    return userRepository.save(existing);
                });
    }

    public Mono<Void> delete(Long id) {
        return userRepository.deleteById(id);
    }

    private void validate(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("User name is required");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("User email is required");
        }
    }
}
