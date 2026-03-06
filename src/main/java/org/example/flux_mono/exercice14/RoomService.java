package org.example.flux_mono.exercice14;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Flux<Room> findAvailableRooms() {
        return roomRepository.findByAvailableTrue();
    }

    public Mono<Room> create(Room room) {
        validate(room);
        room.setId(null);
        if (room.getAvailable() == null) {
            room.setAvailable(true);
        }
        return roomRepository.save(room);
    }

    public Mono<Void> delete(Long id) {
        return roomRepository.deleteById(id);
    }

    public Mono<Room> findById(Long id) {
        return roomRepository.findById(id);
    }

    private void validate(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room is required");
        }
        if (room.getName() == null || room.getName().isBlank()) {
            throw new IllegalArgumentException("Room name is required");
        }
    }
}
