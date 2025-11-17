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
                // CORS alkalmazása
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CSRF kikapcsolása, mert nem kell
                .csrf(csrf -> csrf.disable())

                // Hozzáférés kezelése
                .authorizeHttpRequests(auth -> auth
                        // ezeken még dolgozni, beállítani a végtermékben!
                        // Publikus végpontok (hitelesítés nélkül
                        //.requestMatchers("/api/auth/**").permitAll()  // bejelntkezés, regisztráció, vendégfelhasználó
                        //.requestMatchers("/api/products/**").permitAll()  // termékekhez
                        //.requestMatchers("/h2-console/**").permitAll()  // H2 console-hoz.

                        // Hitelesítést igénylő végpont
                        //.requestMatchers("/api/orders/**").authenticated()  // Rendelések

                        // Bárki..
                        .anyRequest().permitAll()
                )

                // H2 konzol engedélyezése
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Engedélyezett kapcsolati források
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8000",      // Python http szerver
                "http://127.0.0.1:8000",      // Alternatív localhost cím
                "http://localhost:8080",      // 8080-as localhost (springboot)
                "http://localhost:5500",      // előző csak vscode live server portal.
                "http://127.0.0.1:5500"       // VScode live szerver (ez van használva9
        ));

        // Engedélyezett http műveletek listája
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // Engedélyezett fejlécek (header)
        configuration.setAllowedHeaders(Arrays.asList(
                // majd beállítani!
                //"Authorization",
                //"Content-Type",
                //"Accept",
                //"X-Requested-With"
                // !!
                // bármilyen header..
                "*"
        ));

        // Hitelesítésia adatok kezelése..
        configuration.setAllowCredentials(true);

        // Frontend által olvasható fejlécek megmutatása
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization"
        ));

        // preflight kérés élettartama
        configuration.setMaxAge(3600L);

        // minden végponthoz hozzárendlejük a beállítást
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
