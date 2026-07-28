package com.pulse_gym.lb_common.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BiometricJwtService {

    @Value("${biometric.jwt.secret-key}")
    private String secretKey;

    @Value("${biometric.jwt.token-expiration:300000}")
    private Long tokenExpiration;

    /**
     * Convierte la secretKey (Base64 -> bytes) y luego construye la HMAC(HS256)
     * Clave que se usa para firmar y verificar tokens
     * @return SecretKey en formato HMAC
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Intento de parsear el token usando la clave de la firma
     * Si el token es valido (firma correcta, formato correcto) lanza un true
     * @param token
     * @return Boolean true/false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.warn("Token biométrico inválido: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extrae el claim personalizado "userId" del payload del token
     * @param token
     * @return Long userId
     */
    public Long extractUserId(String token) {
        return extractClaims(token, claims -> {
            Number userId = claims.get("userId", Number.class);
            return userId != null ? userId.longValue() : null;
        });
    }

    /**
     * Extrae el claim personalizado "deviceId" del payload del token
     * @param token
     * @return String deviceId
     */
    public String extractDeviceId(String token) {
        return extractClaims(token, claims -> {
            Object deviceId = claims.get("deviceId");
            return deviceId != null ? deviceId.toString() : null;
        });
    }

    /**
     * Referencia del metodo (Claims::getExpiration) para obtener el claim estandar 'exp'
     * @param token
     * @return fecha de expiracion en Date
     */
    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    /**
     * parsea y verifica token con la clave secreta 
     * obtiene el payload (los claims, los datos)
     * @param <T>
     * @param token
     * @param resolver
     * @return Tipado generico
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
     * compara la fecha de expedicion del token, si la expiracion es anterior a "ahora", ya expiro
     * @param token
     * @return boolean
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Genera un hash SHA-256 del deviceId para comparar con el almacenado en la BD.
     * 
     * @param deviceId Identificador plano del dispositivo
     * @return Hash en formato hexadecimal, o null si deviceId es null/vacío
     */
    public String generateHash(String deviceId) {
        if (deviceId == null || deviceId.trim().isEmpty()) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(deviceId.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("Error al generar hash SHA-256: {}", e.getMessage());
            throw new RuntimeException("Error interno al procesar la huella", e);
        }
    }
}