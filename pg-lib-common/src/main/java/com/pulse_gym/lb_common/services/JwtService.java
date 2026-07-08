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

    /**
     * Clave secreta para firmar los tokens JWT (desde application.yaml)
     */
    @Value("${security.jwt.secret-key}")
    String secretKey;

    /**
     * Tiempo de expiración del token en milisegundos (desde application.yaml)
     */
    @Value("${security.jwt.token-expiration}")
    Long tokenExpiration;

    /**
     * Obtiene la clave de firma a partir de la clave secreta en Base64
     * 
     * @return Clave secreta para firmar/verificar JWT
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera un token JWT con los datos del usuario
     * 
     * @param userId ID del usuario
     * @param rol    Rol del usuario
     * @param name   Nombre/email del usuario
     * @return Token JWT firmado
     */
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

    /**
     * Valida si un token JWT es válido (no expirado y firma correcta)
     * 
     * @param token Token JWT a validar
     * @return true si es válido, false en caso contrario
     */
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

    /**
     * Extrae los claims (datos) del token JWT
     * 
     * @param <T>      Tipo de dato a retornar
     * @param token    Token JWT
     * @param resolver Función para extraer un claim específico
     * @return Valor extraído del token
     */
    public <T> T extractClaims(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }

    /**
     * Extrae el nombre de usuario (subject) del token
     * 
     * @param token Token JWT
     * @return Nombre de usuario o email
     */
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * Extrae el ID del usuario del token
     * 
     * @param token Token JWT
     * @return ID del usuario o null si no existe
     */
    public Long extractUserId(String token) {
        return extractClaims(token, claims -> {
            Number userId = claims.get("userId", Number.class);
            return userId != null ? userId.longValue() : null;
        });
    }

    /**
     * Extrae el rol del usuario del token
     * 
     * @param token Token JWT
     * @return Rol del usuario o null si no existe
     */
    public String extractRol(String token) {
        return extractClaims(token, claims -> {
            Object rol = claims.get("rol");
            return rol != null ? rol.toString() : null;
        });
    }

    /**
     * Extrae el email del usuario del token
     * 
     * @param token Token JWT
     * @return Email del usuario o null si no existe
     */
    public String extractGmail(String token) {
        return extractClaims(token, claims -> {
            Object gmail = claims.get("gmail");
            return gmail != null ? gmail.toString() : null;
        });
    }

    /**
     * Renueva un token JWT (refresca la fecha de expiración)
     * 
     * @param token Token JWT actual (puede estar cerca de expirar)
     * @return Nuevo token JWT con fecha renovada
     * @throws Exception Si el token es inválido o está expirado
     */
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

        Number userIdNum = claims.get("userId", Number.class);
        Long userId = userIdNum != null ? userIdNum.longValue() : null;
        Object rolObj = claims.get("rol");
        String rol = rolObj != null ? rolObj.toString() : null;

        return generateToken(userId, rol, claims.getSubject());
    }
}