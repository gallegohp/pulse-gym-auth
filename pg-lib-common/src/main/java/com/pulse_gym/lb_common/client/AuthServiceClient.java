package com.pulse_gym.lb_common.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.pulse_gym.lb_common.dto.AuthUserDTO;
import com.pulse_gym.lb_common.enums.EnumRol;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthServiceClient {

    /** RestTemplate para llamar a auth-service */
    private final RestTemplate restTemplate;

    /** URL del servicio de autenticación */
    private final String authServiceUrl = "http://pg-ms-auth/auth";

    /**
     * Obtiene el rol de un usuario desde auth-service
     * 
     * @param email Email del usuario
     * @return Rol del usuario o null si no se encuentra
     */
    public EnumRol obtenerRolPorEmail(String email) {
        try {
            ResponseEntity<AuthUserDTO> authResponse = restTemplate.getForEntity(
                    authServiceUrl + "/api/internal/users/email/" + email,
                    AuthUserDTO.class);

            if (authResponse.getBody() != null && authResponse.getBody().getRol() != null) {
                return authResponse.getBody().getRol();
            }
        } catch (Exception e) {
            System.err.println("Error al obtener rol para " + email + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene el usuario completo desde auth-service
     * 
     * @param email Email del usuario
     * @return AuthUserDTO o null si no se encuentra
     */
    public AuthUserDTO obtenerUsuarioPorEmail(String email) {
        try {
            ResponseEntity<AuthUserDTO> authResponse = restTemplate.getForEntity(
                    authServiceUrl + "/api/internal/users/email/" + email,
                    AuthUserDTO.class);
            return authResponse.getBody();
        } catch (Exception e) {
            System.err.println("Error al obtener usuario para " + email + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene el usuario completo desde auth-service por ID
     * 
     * @param id ID del usuario
     * @return AuthUserDTO o null si no se encuentra
     */
    public AuthUserDTO obtenerUsuarioPorId(Long id) {
        try {
            ResponseEntity<AuthUserDTO> authResponse = restTemplate.getForEntity(
                    authServiceUrl + "/api/internal/users/id/" + id,
                    AuthUserDTO.class);
            return authResponse.getBody();
        } catch (Exception e) {
            System.err.println("Error al obtener usuario para ID " + id + ": " + e.getMessage());
        }
        return null;
    }
}