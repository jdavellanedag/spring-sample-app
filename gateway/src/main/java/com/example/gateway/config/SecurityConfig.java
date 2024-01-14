package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  public SecurityWebFilterChain securityWebFilterChainConfig (ServerHttpSecurity http) {

    String BASE_PATH = "/api/v1";
    String ORDER_SERVICE_PATH = BASE_PATH + "/orders/**";
    String INVENTORY_SERVICE_PATH = BASE_PATH + "/inventory/**";

    http.csrf().disable();
    http.authorizeExchange()
        .pathMatchers(HttpMethod.GET).permitAll()
        .pathMatchers(HttpMethod.PUT, ORDER_SERVICE_PATH).hasAnyRole("admin", "client")
        .pathMatchers(HttpMethod.POST, ORDER_SERVICE_PATH).hasAnyRole("admin", "client")
        .pathMatchers(HttpMethod.DELETE, ORDER_SERVICE_PATH).hasRole("admin")
        .pathMatchers(HttpMethod.PUT, INVENTORY_SERVICE_PATH).hasRole("admin")
        .pathMatchers(HttpMethod.POST, INVENTORY_SERVICE_PATH).hasRole("admin")
        .pathMatchers(HttpMethod.DELETE, INVENTORY_SERVICE_PATH).hasRole("admin")
        .anyExchange()
        .authenticated()
        .and()
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(jwtAuthenticationConverter())
    ;

    return http.build();
  }

  private ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
    ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(new KeyCloakRoleMapper());
    return converter;
  }

}
