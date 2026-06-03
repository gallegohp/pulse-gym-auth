package com.pulse_gym.lb_common.dto;

import lombok.Data;

@Data
public class ConfiguracionGlobalResponseDTO {

    /**
     * Maximo de notificaciones permitidas por dia por usuario
     */
    private Long maxNotificacionesPorDia;

    /**
     * Maximo de notificaciones permitidas por minuto por usuario
     */
    private Long maxNotificacionesPorMinuto;
}
