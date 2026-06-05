package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RestablecerContrasena {

    /** Token único de recuperación de contraseña */
    @NotBlank(message = "El token es obligatorio")
    private String token;

    /** Nueva contraseña del usuario (mínimo 6 caracteres) */
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String newPassword;

    /** Confirmación de la nueva contraseña (debe coincidir con newPassword) */
    @NotBlank(message = "Debe confirmar la nueva contraseña")
    private String confirmPassword;
}
