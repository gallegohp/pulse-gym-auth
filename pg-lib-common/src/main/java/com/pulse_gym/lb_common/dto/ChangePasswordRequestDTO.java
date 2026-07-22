package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequestDTO {

    /**
     * Contraseña actual del usuario
     */
    @NotBlank(message = "La contraseña actual es obligatoria")
    private String currentPassword;

    /**
     * Nueva contraseña – debe cumplir políticas de seguridad:
     * - Mínimo 8 caracteres
     * - Al menos una mayúscula
     * - Al menos un número
     */
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
        message = "La nueva contraseña debe contener al menos una mayúscula y un número"
    )
    private String newPassword;

    /**
     * Confirmación de la nueva contraseña
     */
    @NotBlank(message = "Debe confirmar la nueva contraseña")
    private String confirmPassword;
}