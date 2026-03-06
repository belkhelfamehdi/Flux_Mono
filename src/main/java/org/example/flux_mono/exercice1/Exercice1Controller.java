package org.example.flux_mono.exercice1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class Exercice1Controller {

    @GetMapping("/exercice1/numbers")
    public Flux<Integer> numbers() {
        return Flux.range(1, 10)
                .map(number -> number * 3)
                .filter(number -> number > 15)
                .doOnNext(System.out::println);
    }
}
