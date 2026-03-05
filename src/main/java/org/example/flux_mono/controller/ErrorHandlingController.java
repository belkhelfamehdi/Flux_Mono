package org.example.flux_mono.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ErrorHandlingController {

    @GetMapping("/error-resume")
    public Flux<String> errorResume() {

        return Flux.just("A", "B", "C", "D")
                .map(letter -> {
                    if (letter.equals("C")) {
                        throw new RuntimeException("Error after C");
                    }
                    return letter;
                })
                .onErrorResume(e -> Flux.just("Default1", "Default2"));
    }

    @GetMapping("/error-continue")
    public Flux<Integer> errorContinue() {

        return Flux.range(1, 5)
                .map(n -> {
                    if (n == 2) {
                        throw new RuntimeException("Error on 2");
                    }
                    return n;
                })
                .onErrorContinue((error, value) -> {
                    System.out.println("Ignored error for value: " + value);
                });
    }

}
