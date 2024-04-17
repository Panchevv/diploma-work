package com.diploma.panchev.apigraphql.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {
    @Getter
    private final String tenant;

    public CustomJwtAuthenticationToken(Jwt jwt, String tenant, Collection<? extends GrantedAuthority> authorities, String name) {
        super(jwt, authorities, name);
        this.tenant = tenant;
    }
}
