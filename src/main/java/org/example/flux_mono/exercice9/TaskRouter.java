package org.example.flux_mono.exercice9;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TaskRouter {

    @Bean
    public RouterFunction<ServerResponse> taskRoutes(TaskHandler taskHandler) {
        return route(GET("/api/tasks"), taskHandler::getAllTasks)
                .andRoute(GET("/api/tasks/{id}"), taskHandler::getTaskById)
                .andRoute(POST("/api/tasks"), taskHandler::createTask)
                .andRoute(PUT("/api/tasks/{id}"), taskHandler::updateTask)
                .andRoute(DELETE("/api/tasks/{id}"), taskHandler::deleteTask);
    }
}
