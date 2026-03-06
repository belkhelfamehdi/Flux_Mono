package org.example.flux_mono.exercice10;

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
public class BookHandler {

    private final Map<Long, Book> books = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    public Mono<ServerResponse> getAllBooks(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Flux.fromIterable(books.values()), Book.class);
    }

    public Mono<ServerResponse> searchByTitle(ServerRequest request) {
        String title = request.queryParam("title").orElse("");

        Flux<Book> matchingBooks = Flux.fromIterable(books.values())
                .filter(book -> book.getTitle() != null
                        && book.getTitle().toLowerCase().contains(title.toLowerCase()));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(matchingBooks, Book.class);
    }

    public Mono<ServerResponse> createBook(ServerRequest request) {
        return request.bodyToMono(Book.class)
                .map(book -> {
                    long newId = idSequence.incrementAndGet();
                    book.setId(newId);
                    books.put(newId, book);
                    return book;
                })
                .flatMap(book -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(book));
    }

    public Mono<ServerResponse> deleteBook(ServerRequest request) {
        long id = Long.parseLong(request.pathVariable("id"));
        Book removed = books.remove(id);

        if (removed == null) {
            return ServerResponse.notFound().build();
        }

        return ServerResponse.noContent().build();
    }
}
