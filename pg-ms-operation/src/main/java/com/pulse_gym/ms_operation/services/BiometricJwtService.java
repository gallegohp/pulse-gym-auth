package com.pulse_gym.ms_operation.services;


import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
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
     * Si el tokean es valido (firma correcta, formato correcto) lanza un true
     * @param token
     * @return Boolean true/false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Extrae el claim personalizado "userId" del payload del token
     * se usa number porque JSON puede desifrar varios formatios (Long, Integer)
     * luego de convierte usando el .longValue
     * @param token
     * @return
     */
    public Long extractUserId(String token) {
        return extractClaims(token, claims -> {
            Number userId = claims.get("userId", Number.class);
            return userId != null ? userId.longValue() : null;
        });
    }

    /**
     * Referencia del metodo (Claims::getExpiration) para obtener el claim estandar 'exp', del token, convertirlo a Date
     * @param token
     * @return fecha de expiracion en Date
     */
    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    /**
     * parsea y verifica token con la clave secreta 
     * obtiene el payload (los claims, los datos)
     * Funcion resolver (patron funcional que permite reutilizar "parsear token")
     * para extraer cualquier dato (userId, expiration o lo que sea)
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
     * @return bookean
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
