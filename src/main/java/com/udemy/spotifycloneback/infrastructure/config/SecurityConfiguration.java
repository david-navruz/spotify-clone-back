package com.udemy.spotifycloneback.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "api/songs").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/songs/search").permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf ->
                        csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .oauth2Client(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Replace with your Auth0 issuer URI
        String issuerUri = "https://dev-aoem2zhhrgr1yl6i.us.auth0.com/";
        return NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    }

}
