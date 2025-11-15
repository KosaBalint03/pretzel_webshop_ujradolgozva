package com.pretzel.backend.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Enable CORS with custom configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Disable CSRF (since you're building an API, not form-based auth)
                .csrf(csrf -> csrf.disable())

                // Configure authorization
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (no authentication required)
                        //.requestMatchers("/api/auth/**").permitAll()  // Login, register, guest
                        //.requestMatchers("/api/products/**").permitAll()  // View products
                        //.requestMatchers("/h2-console/**").permitAll()  // H2 console (dev only)

                        // Protected endpoints (authentication required)
                        //.requestMatchers("/api/orders/**").authenticated()  // Orders need auth

                        // Everything else
                        .anyRequest().permitAll()
                )

                // Allow H2 console to work (dev only)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed origins (where your frontend runs)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8000",      // Python http.server
                "http://127.0.0.1:8000",      // Alternative localhost
                "http://localhost:8080",      // If serving from Spring Boot
                "http://localhost:5500",      // VS Code Live Server
                "http://127.0.0.1:5500"       // VS Code Live Server alternative!!
        ));

        // Allowed HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // Allowed headers
        configuration.setAllowedHeaders(Arrays.asList(
                //"Authorization",
                //"Content-Type",
                //"Accept",
                //"X-Requested-With"
                // !!
                "*"
        ));

        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);

        // Expose headers that frontend can read
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization"
        ));

        // How long the preflight request can be cached (in seconds)
        configuration.setMaxAge(3600L);

        // Apply this configuration to all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
