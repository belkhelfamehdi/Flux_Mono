package org.example.flux_mono.exercice12;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

    Flux<Order> findByStatusIgnoreCase(String status);

    Flux<Order> findByCustomerNameIgnoreCase(String customerName);
}
