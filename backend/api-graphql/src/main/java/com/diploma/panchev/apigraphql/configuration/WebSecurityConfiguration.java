package com.diploma.panchev.apigraphql.configuration;

import com.diploma.panchev.apigraphql.auth.CustomJwtAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:3000"); // Allow frontend origin
        corsConfig.addAllowedMethod("*"); // Allow all HTTP methods
        corsConfig.addAllowedHeader("*"); // Allow all headers
        corsConfig.setAllowCredentials(true); // Allow cookies and credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsFilter(source);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
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
                )
                .addFilterBefore(corsFilter(), CorsFilter.class); // Add CorsFilter before security filters

        return http.build();
    }
}
