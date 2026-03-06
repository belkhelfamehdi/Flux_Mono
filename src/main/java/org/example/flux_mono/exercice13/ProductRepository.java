package org.example.flux_mono.exercice13;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    Flux<Product> findByNameContainingIgnoreCase(String name);

    @Modifying
    @Query("UPDATE \"products\" SET stock = stock - :quantity WHERE id = :id AND stock >= :quantity")
    Mono<Integer> decrementStockIfAvailable(@Param("id") Long id, @Param("quantity") int quantity);
}
