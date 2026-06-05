package com.pulse_gym.lb_common.dto;

import com.pulse_gym.lb_common.enums.EnumCanalNotificacion;
import com.pulse_gym.lb_common.enums.EnumEventoAsociado;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerificarPreferenciaRequestDTO {

    /**
     * Identificador del usuario en auth
     */
    @NotNull(message = "El id de usuario es obligatorio")
    private Long usuarioId;

    /**
     * Tipo de evento de la notificacion
     */
    @NotNull(message = "El tipo de evento es obligatorio")
    private EnumEventoAsociado tipoEvento;

    /**
     * Canal por el que se desea enviar la notificacion
     */
    @NotNull(message = "El canal es obligatorio")
    private EnumCanalNotificacion canal;
}
