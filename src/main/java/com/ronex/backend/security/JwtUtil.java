package com.ronex.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET =
            "RONEX_SUPER_SECRET_KEY_12345678901234567890";

    private static final long EXPIRATION =
            1000L * 60 * 60 * 24; // 24 Hours

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // ==========================
    // GENERATE JWT
    // ==========================
    public String generateToken(String username, String role) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ==========================
    // VALIDATE JWT
    // ==========================
    public boolean validateToken(String token) {

        try {

            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            System.out.println("✅ JWT VALID");

            return true;

        } catch (ExpiredJwtException e) {

            System.out.println("❌ JWT EXPIRED");
            e.printStackTrace();

        } catch (UnsupportedJwtException e) {

            System.out.println("❌ UNSUPPORTED JWT");
            e.printStackTrace();

        } catch (MalformedJwtException e) {

            System.out.println("❌ MALFORMED JWT");
            e.printStackTrace();

        } catch (SecurityException e) {

            System.out.println("❌ INVALID SIGNATURE");
            e.printStackTrace();

        } catch (IllegalArgumentException e) {

            System.out.println("❌ EMPTY JWT");
            e.printStackTrace();

        } catch (Exception e) {

            System.out.println("❌ UNKNOWN JWT ERROR");
            e.printStackTrace();

        }

        return false;
    }

    // ==========================
    // GET USERNAME
    // ==========================
    public String extractUsername(String token) {

        return getClaims(token).getSubject();

    }

    // ==========================
    // GET ROLE
    // ==========================
    public String extractRole(String token) {

        return getClaims(token).get("role", String.class);

    }

    // ==========================
    // GET CLAIMS
    // ==========================
    private Claims getClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }
}