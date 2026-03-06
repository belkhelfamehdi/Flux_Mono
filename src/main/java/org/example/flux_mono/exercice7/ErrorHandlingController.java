package org.example.flux_mono.exercice7;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class ErrorHandlingController {

    @GetMapping("/error-resume")
    public Flux<String> errorResume() {
        return Flux.just("A", "B", "C", "D")
                .map(letter -> {
                    if ("C".equals(letter)) {
                        throw new RuntimeException("Error after C");
                    }
                    return letter;
                })
                .onErrorResume(error -> Flux.just("Default1", "Default2"));
    }

    @GetMapping("/error-continue")
    public Flux<Integer> errorContinue() {
        return Flux.range(1, 5)
                .map(number -> {
                    if (number == 2) {
                        throw new RuntimeException("Error on 2");
                    }
                    return number;
                })
                .onErrorContinue((error, value) -> {
                });
    }
}
