package org.example.flux_mono.exercice14;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.POST, "/api/rooms").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/rooms/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/rooms").authenticated()
                        .anyExchange().permitAll())
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .authenticationEntryPoint(unauthorizedEntryPoint())
                        .accessDeniedHandler(forbiddenHandler()))
                .build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("{noop}user123")
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password("{noop}admin123")
                .roles("ADMIN", "USER")
                .build();

        return new MapReactiveUserDetailsService(user, admin);
    }

    private ServerAuthenticationEntryPoint unauthorizedEntryPoint() {
        return (exchange, ex) -> {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            String body = "{\"error\":\"UNAUTHORIZED\",\"message\":\"Vous devez vous connecter pour accéder à cette ressource\"}";
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            return exchange.getResponse().writeWith(
                    reactor.core.publisher.Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
        };
    }

    private ServerAccessDeniedHandler forbiddenHandler() {
        return (exchange, denied) -> {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            String body = "{\"error\":\"FORBIDDEN\",\"message\":\"Vous n'avez pas les droits nécessaires pour accéder à cette ressource\"}";
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            return exchange.getResponse().writeWith(
                    reactor.core.publisher.Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
        };
    }
}
