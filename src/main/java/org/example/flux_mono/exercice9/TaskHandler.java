package org.example.flux_mono.exercice9;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TaskHandler {

    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    public Mono<ServerResponse> getAllTasks(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Flux.fromIterable(tasks.values()), Task.class);
    }

    public Mono<ServerResponse> getTaskById(ServerRequest request) {
        long id = Long.parseLong(request.pathVariable("id"));
        Task task = tasks.get(id);

        if (task == null) {
            return ServerResponse.notFound().build();
        }

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(task);
    }

    public Mono<ServerResponse> createTask(ServerRequest request) {
        return request.bodyToMono(Task.class)
                .map(task -> {
                    long newId = idSequence.incrementAndGet();
                    task.setId(newId);
                    tasks.put(newId, task);
                    return task;
                })
                .flatMap(task -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(task));
    }

    public Mono<ServerResponse> updateTask(ServerRequest request) {
        long id = Long.parseLong(request.pathVariable("id"));

        if (!tasks.containsKey(id)) {
            return ServerResponse.notFound().build();
        }

        return request.bodyToMono(Task.class)
                .map(updatedTask -> {
                    Task current = tasks.get(id);
                    current.setDescription(updatedTask.getDescription());
                    current.setCompleted(updatedTask.isCompleted());
                    tasks.put(id, current);
                    return current;
                })
                .flatMap(task -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(task));
    }

    public Mono<ServerResponse> deleteTask(ServerRequest request) {
        long id = Long.parseLong(request.pathVariable("id"));
        Task removed = tasks.remove(id);

        if (removed == null) {
            return ServerResponse.notFound().build();
        }

        return ServerResponse.noContent().build();
    }
}
