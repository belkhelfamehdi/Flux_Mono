package org.example.flux_mono.exercice14;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public Flux<Room> getAvailableRooms() {
        return roomService.findAvailableRooms();
    }

    @PostMapping
    public Mono<ResponseEntity<Room>> createRoom(@RequestBody Room room) {
        return Mono.defer(() -> roomService.create(room))
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRoom(@PathVariable Long id) {
        return roomService.findById(id)
                .flatMap(existing -> roomService.delete(id)
                        .thenReturn(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
