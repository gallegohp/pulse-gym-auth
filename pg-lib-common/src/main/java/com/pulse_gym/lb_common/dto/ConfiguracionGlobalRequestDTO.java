package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfiguracionGlobalRequestDTO {

    /**
     * Maximo de notificaciones permitidas por dia por usuario
     */
    @NotNull(message = "El maximo por dia es obligatorio")
    @Min(value = 1, message = "El maximo por dia debe ser al menos 1")
    private Long maxNotificacionesPorDia;

    /**
     * Maximo de notificaciones permitidas por minuto por usuario
     */
    @NotNull(message = "El maximo por minuto es obligatorio")
    @Min(value = 1, message = "El maximo por minuto debe ser al menos 1")
    private Long maxNotificacionesPorMinuto;
}
