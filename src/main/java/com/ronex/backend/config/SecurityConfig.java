package com.ronex.backend.config;

import com.ronex.backend.security.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(ex -> ex

                        // 401 - Not authenticated
                        .authenticationEntryPoint(
                                (req, res, e) ->
                                        res.sendError(
                                                HttpServletResponse.SC_UNAUTHORIZED
                                        )
                        )

                        // 403 - Authenticated but wrong role
                        .accessDeniedHandler(
                                (req, res, e) ->
                                        res.sendError(
                                                HttpServletResponse.SC_FORBIDDEN
                                        )
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // ==========================================
                        // PUBLIC / AUTH
                        // ==========================================

                        .requestMatchers(
                                "/auth/**",
                                "/api/auth/**",
                                "/cloudinary/**",
                                "/uploads/**",
                                "/ws/**",
                                "/sockjs/**",
                                "/actuator/**"
                        ).permitAll()

                        // ==========================================
                        // PUBLIC REELS FEED
                        // ==========================================

                        .requestMatchers(HttpMethod.GET, "/api/reels/**")
                        .permitAll()

                        // ==========================================
                        // AUTHENTICATED REELS ACTIONS
                        // ==========================================

                        .requestMatchers(HttpMethod.POST, "/api/reels/**")
                        .authenticated()

                        // ==========================================
                        // NORMAL USER APIs
                        // ==========================================

                        .requestMatchers("/api/user/**")
                        .authenticated()

                        // ==========================================
                        // ADMIN APIs
                        // ==========================================

                        .requestMatchers("/api/admin/**")
                        .hasAuthority("ADMIN")

                        // ==========================================
                        // MANAGER APIs
                        // ==========================================

                        .requestMatchers("/api/manager/**")
                        .hasAuthority("MANAGER")

                        // ==========================================
                        // AGENT APIs
                        // ==========================================

                        .requestMatchers("/api/agent/**")
                        .hasAuthority("AGENT")

                        // ==========================================
                        // EVERYTHING ELSE
                        // ==========================================

                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}