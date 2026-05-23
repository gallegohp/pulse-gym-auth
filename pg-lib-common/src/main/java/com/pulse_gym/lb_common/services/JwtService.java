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
     * Clave secreta utilizada para firmar y verificar los tokens JWT, se obtiene de la configuración de la aplicación
     */
    @Value("${security.jwt.secret-key}")
    String secretKey;

    /**
     * Duración en milisegundos de la validez de los tokens JWT, se obtiene de la configuración de la aplicación
     */
    @Value("${security.jwt.token-expiration}")
    Long tokenExpiration;

    /**
     * Genera la clave de firma a partir de la clave secreta configurada, decodificando la clave en Base64 y creando un objeto SecretKey adecuado para la firma de tokens JWT
     * @return
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera un token JWT con los reclamos proporcionados (userId, rol, name) y lo firma utilizando la clave secreta configurada. El token incluye una fecha de emisión y una fecha de expiración calculada en función del tiempo actual y la duración configurada para los tokens.
     * @param userId
     * @param rol
     * @param name
     * @return El token JWT generado como una cadena de texto
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
     * Valida un token JWT verificando su firma y asegurándose de que no haya expirado. Si el token es válido, devuelve true; si el token es inválido o ha expirado, devuelve false. Se manejan específicamente las excepciones JwtException para tokens inválidos y ExpiredJwtException para tokens expirados, así como una captura general de Exception para cualquier otro error que pueda ocurrir durante la validación del token.
     * @param token
     * @return true si el token es válido, false si el token es inválido o ha expirado
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
     * Extrae los reclamos de un token JWT utilizando una función de resolución personalizada. La función resolver se aplica a los reclamos extraídos del token para obtener el valor deseado. Este método permite extraer cualquier información contenida en los reclamos del token de manera flexible, dependiendo de la función de resolución proporcionada.
     * @param <T>
     * @param token
     * @param resolver
     * @return El valor extraído de los reclamos del token utilizando la función de resolución proporcionada
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
     * Extrae el nombre de usuario (subject) de un token JWT utilizando la función de resolución para obtener el valor del reclamo "sub" (subject) del token. Este método es una forma conveniente de obtener el nombre de usuario asociado con el token JWT, asumiendo que el nombre de usuario se almacena en el reclamo "sub" del token.
     * @param token
     * @return El nombre de usuario extraído del token JWT, o null si el reclamo "sub" no está presente en el token
     */
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * Extrae el ID de usuario del token JWT utilizando la función de resolución para obtener el valor del reclamo "userId" del token. Este método asume que el ID de usuario se almacena en el reclamo "userId" del token y devuelve su valor como un Long. Si el reclamo "userId" no está presente en el token o no es un número, devuelve null.
     * @param token
     * @return El ID de usuario extraído del token JWT como un Long, o null si el reclamo "userId" no está presente o no es un número válido en el token
     */
    public Long extractUserId(String token) {
        return extractClaims(token, claims -> {
            Number userId = claims.get("userId", Number.class);
            return userId != null ? userId.longValue() : null;
        });
    }

    /**
     * Extrae el rol del usuario del token JWT utilizando la función de resolución para obtener el valor del reclamo "rol" del token. Este método asume que el rol del usuario se almacena en el reclamo "rol" del token y devuelve su valor como una cadena de texto. Si el reclamo "rol" no está presente en el token, devuelve null.
     * @param token
     * @return El rol del usuario extraído del token JWT como una cadena de texto, o null si el reclamo "rol" no está presente en el token  
     */
    public String extractRol(String token) {
        return extractClaims(token, claims -> {
            Object rol = claims.get("rol");
            return rol != null ? rol.toString() : null;
        });
    }

    /**
     * Refresca un token JWT existente generando un nuevo token con los mismos reclamos (userId, rol, subject) pero con una nueva fecha de emisión y una nueva fecha de expiración. El método primero intenta extraer los reclamos del token proporcionado, manejando específicamente las excepciones ExpiredJwtException para tokens expirados y JwtException para tokens inválidos. Si el token es válido o ha expirado, se extraen los reclamos necesarios para generar un nuevo token utilizando el método generateToken. Si ocurre cualquier otra excepción durante el proceso, se lanza una excepción genérica con un mensaje de error correspondiente.
     * @param token
     * @return
     * @throws Exception si el token es inválido o ha expirado, con un mensaje de error que describe la razón del fallo
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

        // Extracción segura del ID numérico evitando ClassCastException
        Number userIdNum = claims.get("userId", Number.class);
        Long userId = userIdNum != null ? userIdNum.longValue() : null;
        Object rolObj = claims.get("rol");
        String rol = rolObj != null ? rolObj.toString() : null;

        return generateToken(userId, rol, claims.getSubject());
    }
}