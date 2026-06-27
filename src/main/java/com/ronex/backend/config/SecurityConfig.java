package com.ronex.backend.config;

import com.ronex.backend.security.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .exceptionHandling(ex -> ex.authenticationEntryPoint(
                (req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            ))

            .authorizeHttpRequests(auth -> auth

                // 🔓 AUTH
                .requestMatchers(
                    "/auth/**",
                    "/api/auth/**"
                ).permitAll()

                // 🔓 STATIC FILES ONLY
                .requestMatchers("/uploads/**").permitAll()

                // 🔐 REELS NEED JWT
                .requestMatchers("/api/reels/**").authenticated()

                // 🔓 WS
                .requestMatchers("/ws/**", "/sockjs/**").permitAll()

                // 🔓 ACTUATOR
                .requestMatchers("/actuator/**").permitAll()

                // 🔐 ROLES
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/user/**").hasRole("USER")

                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}