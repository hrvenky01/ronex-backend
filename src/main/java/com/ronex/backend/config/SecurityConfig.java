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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {

        http
                // ==========================================
                // CSRF
                // ==========================================

                .csrf(csrf -> csrf.disable())

                // ==========================================
                // CORS
                // ==========================================

                .cors(Customizer.withDefaults())

                // ==========================================
                // DISABLE BASIC AUTH
                // ==========================================

                .httpBasic(basic -> basic.disable())

                // ==========================================
                // DISABLE FORM LOGIN
                // ==========================================

                .formLogin(form -> form.disable())

                // ==========================================
                // STATELESS JWT SESSION
                // ==========================================

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // ==========================================
                // EXCEPTION HANDLING
                // ==========================================

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

                // ==========================================
                // AUTHORIZATION
                // ==========================================

                .authorizeHttpRequests(auth -> auth

                        // ==================================
                        // CORS PREFLIGHT
                        // ==================================

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // ==================================
                        // PUBLIC / AUTH
                        // ==================================

                        .requestMatchers(
                                "/auth/**",
                                "/api/auth/**",
                                "/cloudinary/**",
                                "/uploads/**",
                                "/ws/**",
                                "/sockjs/**",
                                "/actuator/**"
                        ).permitAll()

                        // ==================================
                        // PUBLIC REELS FEED
                        // ==================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/reels/**"
                        ).permitAll()

                        // ==================================
                        // AUTHENTICATED REELS ACTIONS
                        // ==================================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/reels/**"
                        ).authenticated()

                        // ==================================
                        // NORMAL USER APIs
                        // ==================================

                        .requestMatchers(
                                "/api/user/**"
                        ).authenticated()

                        // ==================================
                        // ADMIN APIs
                        // ==================================

                        .requestMatchers(
                                "/api/admin/**"
                        ).hasAuthority("ADMIN")

                        // ==================================
                        // MANAGER APIs
                        // ==================================

                        .requestMatchers(
                                "/api/manager/**"
                        ).hasAuthority("MANAGER")

                        // ==================================
                        // AGENT APIs
                        // ==================================

                        .requestMatchers(
                                "/api/agent/**"
                        ).hasAuthority("AGENT")

                        // ==================================
                        // EVERYTHING ELSE
                        // ==================================

                        .anyRequest()
                        .authenticated()
                )

                // ==========================================
                // JWT FILTER
                // ==========================================

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }


    // ======================================================
    // CORS CONFIGURATION
    // ======================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        // RONEX ADMIN FRONTEND
        configuration.setAllowedOrigins(
                List.of(
                        "https://ronex-admin.onrender.com"
                )
        );

        // Allowed HTTP methods
        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        // Allowed request headers
        configuration.setAllowedHeaders(
                List.of(
                        "Authorization",
                        "Content-Type",
                        "Accept",
                        "Origin",
                        "X-Requested-With"
                )
        );

        // Response headers that browser can access
        configuration.setExposedHeaders(
                List.of(
                        "Authorization"
                )
        );

        // Allow credentials
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}