package com.diploma.panchev.apigraphql.auth;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, CustomJwtAuthenticationToken> {
    private final Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public CustomJwtAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = this.jwtGrantedAuthoritiesConverter.convert(jwt);
        String tenantClaimName = "tenant";
        String tenant = jwt.hasClaim(tenantClaimName) ? jwt.getClaimAsString("tenant") : "local";
        String principalClaimValue = jwt.getClaimAsString(JwtClaimNames.SUB);
        return new CustomJwtAuthenticationToken(jwt, tenant, authorities, principalClaimValue);
    }
}
