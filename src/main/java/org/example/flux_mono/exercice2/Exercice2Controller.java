package org.example.flux_mono.exercice2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api")
public class Exercice2Controller {

    @GetMapping("/exercice2/retry")
    public Mono<String> retrySimulation() {
        AtomicInteger attempts = new AtomicInteger(0);

        return Mono.fromCallable(() -> {
                    int currentAttempt = attempts.incrementAndGet();
                    if (currentAttempt <= 2) {
                        throw new RuntimeException("Erreur reseau");
                    }
                    return "Succes!";
                })
                .doOnError(error -> System.out.println("Tentative " + attempts.get() + ": " + error.getMessage()))
                .retry(2)
                .doOnNext(result -> System.out.println("Tentative " + attempts.get() + ": " + result))
                .onErrorResume(error -> Mono.just(error.getMessage()));
    }
}
