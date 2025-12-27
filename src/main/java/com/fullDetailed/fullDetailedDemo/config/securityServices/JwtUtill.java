package com.fullDetailed.fullDetailedDemo.config.securityServices;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtill {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;


    private byte[] getSigningKey() {
        return secret.getBytes();
    }

    public String generateToken(CustomUserDetails userDetails) {

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(getSigningKey()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        long refreshExpirationMs = jwtExpiration * 7;

        return Jwts.builder()
                .claim("roles", roles)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(Keys.hmacShaKeyFor(getSigningKey()), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(getSigningKey()))
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(getSigningKey()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("roles", List.class);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(getSigningKey()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);
    }
}
