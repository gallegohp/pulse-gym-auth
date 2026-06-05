package com.pulse_gym.lb_common.dto;

import java.util.Map;

import com.pulse_gym.lb_common.enums.EnumEventoAsociado;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnvioEventoNotificacionDTO {

    /**
     * Identificador del usuario en auth
     */
    @NotNull(message = "El id de usuario es obligatorio")
    private Long usuarioId;

    /**
     * Evento que dispara la notificacion
     */
    @NotNull(message = "El evento es obligatorio")
    private EnumEventoAsociado evento;

    /**
     * Variables adicionales para la plantilla
     */
    private Map<String, Object> variablesAdicionales;
}
