package com.ronex.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();

        return path.startsWith("/auth/")
                || path.startsWith("/api/auth/")
                || path.startsWith("/cloudinary/")
                || path.startsWith("/uploads/")
                || path.startsWith("/ws/")
                || path.startsWith("/sockjs/")
                || path.startsWith("/actuator/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println("\n========================================");
        System.out.println("🔥 JWT FILTER HIT");
        System.out.println("URI = " + request.getRequestURI());

        String header = request.getHeader("Authorization");

        System.out.println("HEADER = " + header);

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            System.out.println("TOKEN = " + token);

            try {

                boolean valid = jwtUtil.validateToken(token);

                System.out.println("VALID = " + valid);

                if (valid) {

                    String username = jwtUtil.extractUsername(token);
                    String role = jwtUtil.extractRole(token);

                    System.out.println("USERNAME = " + username);
                    System.out.println("ROLE = " + role);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    List.of(new SimpleGrantedAuthority(role))
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    System.out.println("✅ AUTHENTICATED");

                } else {

                    System.out.println("❌ INVALID TOKEN");

                }

            } catch (Exception e) {

                System.out.println("❌ JWT ERROR");
                e.printStackTrace();

            }

        } else {

            System.out.println("❌ AUTHORIZATION HEADER MISSING");

        }

        System.out.println("========================================\n");

        filterChain.doFilter(request, response);
    }
}