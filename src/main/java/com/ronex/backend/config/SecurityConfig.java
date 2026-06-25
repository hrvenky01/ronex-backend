package com.ronex.backend.config;

import com.ronex.backend.security.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
            // ❌ Disable defaults
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())

            // ✅ JWT = STATELESS
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // ✅ 401 instead of redirect
            .exceptionHandling(ex -> ex.authenticationEntryPoint(
                (req, res, e) ->
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            ))

            .authorizeHttpRequests(auth -> auth

                // 🔓 PUBLIC AUTH
                .requestMatchers(
                    "/auth/send-otp",
                    "/auth/verify-otp",
                    "/auth/login",
                    "/auth/register",
                    "/api/auth/**"
                ).permitAll()

                // 🔓 🔥 REELS + VIDEO FILES (IMPORTANT)
                .requestMatchers(
                    "/api/reels/**",
                    "/uploads/**"
                ).permitAll()

                // 🔓 WEBSOCKET
                .requestMatchers(
                    "/ws/**",
                    "/sockjs/**",
                    "/topic/**",
                    "/app/**"
                ).permitAll()

                // 🔓 ACTUATOR
                .requestMatchers("/actuator/**").permitAll()

                // 🔐 ADMIN
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // 🔐 USER
                .requestMatchers("/api/user/**").hasRole("USER")

                // 🔒 OTHERS
                .anyRequest().authenticated()
            )

            // ✅ JWT FILTER
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}