package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistroAsistenciaBiometricaDTO {
    
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El token biometrio es obligatorio")
    private String token;

}
