package com.diploma.panchev.apigraphql.configuration;

import com.diploma.panchev.apigraphql.auth.CustomJwtAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http//
                .authorizeHttpRequests(//
                        authorize -> authorize//
                                .requestMatchers(HttpMethod.POST, "/graphql")
                                .authenticated()
                                .requestMatchers(HttpMethod.GET, "/graphql")
                                .permitAll()
                        //
                        // Other paths are not protected
                )
                .oauth2ResourceServer(x ->
                        x.jwt(configurer ->
                                configurer.jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())
                        )
                );

        return http.build();
    }
}
