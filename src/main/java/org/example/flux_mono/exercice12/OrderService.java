package org.example.flux_mono.exercice12;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Flux<Order> findAll() {
        return orderRepository.findAll();
    }

    public Mono<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Mono<Order> create(Order order) {
        order.setId(null);
        order.setCreatedAt(LocalDateTime.now());

        if (order.getStatus() == null || order.getStatus().isBlank()) {
            order.setStatus("PENDING");
        }

        return orderRepository.save(order);
    }

    public Mono<Order> updateStatus(Long id, String status) {
        return orderRepository.findById(id)
                .flatMap(existing -> {
                    existing.setStatus(status);
                    return orderRepository.save(existing);
                });
    }

    public Mono<Void> deleteById(Long id) {
        return orderRepository.deleteById(id);
    }

    public Flux<Order> findByStatus(String status) {
        return orderRepository.findByStatusIgnoreCase(status);
    }

    public Flux<Order> findByCustomerName(String customerName) {
        return orderRepository.findByCustomerNameIgnoreCase(customerName);
    }

    public Flux<Order> findPaged(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 5 : size;

        long toSkip = (long) safePage * safeSize;
        return orderRepository.findAll()
                .skip(toSkip)
                .take(safeSize);
    }
}
