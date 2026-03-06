package org.example.flux_mono.exercice12;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

    Flux<Order> findByStatusIgnoreCase(String status);

    Flux<Order> findByCustomerNameIgnoreCase(String customerName);

    @Query("SELECT * FROM \"orders\" ORDER BY id LIMIT :limit OFFSET :offset")
    Flux<Order> findPaged(@Param("limit") int limit, @Param("offset") long offset);
}
