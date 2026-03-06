package org.example.flux_mono.exercice6;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class OperationController {

    @GetMapping("/processed-numbers")
    public Flux<String> getProcessedNumbers() {
        return Flux.range(1, 10)
                .filter(number -> number % 2 == 0)
                .map(number -> number * 10)
                .map(number -> "Processed: " + number);
    }
}
