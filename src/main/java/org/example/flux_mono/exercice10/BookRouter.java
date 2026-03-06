package org.example.flux_mono.exercice10;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class BookRouter {

    @Bean
    public RouterFunction<ServerResponse> bookRoutes(BookHandler bookHandler) {
        return route(GET("/api/books"), bookHandler::getAllBooks)
                .andRoute(GET("/api/books/search"), bookHandler::searchByTitle)
                .andRoute(POST("/api/books"), bookHandler::createBook)
                .andRoute(DELETE("/api/books/{id}"), bookHandler::deleteBook);
    }
}
