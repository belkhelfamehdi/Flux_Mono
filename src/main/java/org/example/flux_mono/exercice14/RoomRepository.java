package org.example.flux_mono.exercice14;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface RoomRepository extends ReactiveCrudRepository<Room, Long> {

    Flux<Room> findByAvailableTrue();
}
