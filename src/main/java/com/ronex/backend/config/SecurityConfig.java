package com.ronex.backend.config;

import com.ronex.backend.security.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
            .cors(cors -> cors.disable())
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())

            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(
                    (req, res, e) ->
                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                )
            )

            .authorizeHttpRequests(auth -> auth
                // ✅ PUBLIC
                .requestMatchers("/api/auth/**").permitAll()

                // 🔐 ADMIN
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // 🔐 USER
                .requestMatchers("/api/user/**").hasRole("USER")

                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}