package com.example.gateway.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public class KeyCloakRoleMapper implements Converter<Jwt, Flux<GrantedAuthority>> {

  @Override
  public Flux<GrantedAuthority> convert(Jwt source) {
    final Map<String, List<String>> realmAccess = (Map<String, List<String>>) source.getClaims().get("realm_access");
    return Flux.fromStream(realmAccess.get("roles").stream().map(role -> "ROLE_" + role).map(SimpleGrantedAuthority::new));
  }
}
