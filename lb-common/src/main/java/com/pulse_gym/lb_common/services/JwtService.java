package com.pulse_gym.lb_common.services;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    @Value("${security.jwt.secret-key}") 
    String secretKey;
    
    @Value("${security.jwt.token-expiration}") 
    Long tokenExpiration;

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Long userId, String rol, String name) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("rol", rol)
                .subject(name)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSignKey())
                .compact();
    }

    public Boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public <T> T extractClaims(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Long extractUserId(String token) {
        return extractClaims(token, claims -> {
            Number userId = claims.get("userId", Number.class);
            return userId != null ? userId.longValue() : null;
        });
    }

    public String extractRol(String token) {
        return extractClaims(token, claims -> claims.get("rol", String.class));
    }

    // ✅ CORREGIDO: Extracción segura de tipos numéricos al refrescar
    public String refreshToken(String token) throws Exception {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new Exception("Token is expired");
        } catch (JwtException e) {
            throw new Exception("Token is invalid");
        }

        // Extracción segura del ID numérico evitando ClassCastException
        Number userIdNum = claims.get("userId", Number.class);
        Long userId = userIdNum != null ? userIdNum.longValue() : null;
        String rol = claims.get("rol", String.class);

        return generateToken(userId, rol, claims.getSubject());
    }
}