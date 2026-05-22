package com.pulse_gym.ms_auth.dto;

import com.pulse_gym.lb_common.enums.EnumRol;

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
    private EnumRol rol;

    /**
     * Estado del usuario
     */
    private Boolean estado;

}
