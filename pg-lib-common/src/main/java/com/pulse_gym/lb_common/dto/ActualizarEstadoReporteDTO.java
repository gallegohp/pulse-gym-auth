// ActualizarEstadoReporteDTO.java
package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarEstadoReporteDTO {
    @NotNull(message = "El estado es obligatorio")
    private String estado; // PENDIENTE, EN_REVISION, EN_REPARACION, RESUELTO
}