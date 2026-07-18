package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudTokenBiometricoDTO {

    /**
     * ID del usuario (socio) que solicita el token biométrico.
     */
    @NotNull(message = "El ID de usuario es obligatorio")
    @Positive(message = "El ID de usuario debe ser un número positivo")
    private Long userId;

    /**
     * Identificador del dispositivo biométrico (deviceId plano).
     * Será hasheado en el backend antes de generar el token.
     */
    @NotNull(message = "El deviceId es obligatorio")
    private String deviceId;
}