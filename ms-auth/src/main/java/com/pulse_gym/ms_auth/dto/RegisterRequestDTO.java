package com.pulse_gym.ms_auth.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    /**
     * Nombre del email
     */
    private String email;

    /**
     * Contraseña del usuario
     */
    private String password;

    /**
     * username del usuario
     */
    private String username;

    /**
     * Rol del usuario
     */
    private Long rol;

    /**
     * Estado del usuario
     */
    private Boolean estado;

}
